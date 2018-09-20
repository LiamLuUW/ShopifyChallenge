package com.liam.shopifychallenge;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * A Fragment on main activity it shows the detail page of selected product.
 * Logic:
 *      get parcelable product data from parent fragment and display to user. Then a background task
 *      will call the API to get this product's latest data again from server. Then it will compare
 *      the cached and latest product data, if there is a difference, it will notify the parent
 *      fragment's listener to update the full list upon on return.
 */

public class ProductDetailFragment extends Fragment  {

    private final static String TAG = "ProductDetailFragment";
    private final static String PRODUCT_DATA_KAY = "ProductDetailFragment.product";
    private Product mProduct;
    private static UpdateTaskResultListener taskCompleteListener;

    private TextView title;
    private TextView description;
    private Spinner spinner;
    private TextView vendor;
    private ImageView image;
    private TextView price;
    private TextView weight;
    private TextView weightUnit;
    private TextView quantity;
    private TextView taxable;


    public static ProductDetailFragment newInstance(Product product) {
        final Bundle args = new Bundle();
        args.putParcelable(PRODUCT_DATA_KAY, product);
        final ProductDetailFragment productDetailFragment = new ProductDetailFragment();
        productDetailFragment.setArguments(args);
        return productDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        Log.i(TAG, "ProductDetailFragment created");
        super.onCreate(savedInstance);
        final Bundle args = getArguments();
        if(args != null) {
            mProduct = args.getParcelable(PRODUCT_DATA_KAY);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        final View view = inflater.inflate(R.layout.product_detail_view, container, false);

        title = view.findViewById(R.id.product_detail_title);
        description = view.findViewById(R.id.product_detail_description);
        image = view.findViewById(R.id.product_detail_image);
        spinner = view.findViewById(R.id.product_detail_selection);
        vendor = view.findViewById(R.id.product_detail_vendor);
        price = view.findViewById(R.id.product_detail_price);
        weight = view.findViewById(R.id.product_detail_weight);
        weightUnit = view.findViewById(R.id.product_detail_weight_unit);
        quantity = view.findViewById(R.id.product_detail_quantity);
        taxable = view.findViewById(R.id.product_detail_taxable);

        title.setText(mProduct.getTitle());
        description.setText(mProduct.getBody_html());
        // set up spinner content
        List<ProductVariant> variants = mProduct.getVariants();
        List<String> variantList = new ArrayList<>();
        if(variants == null) {
            // no need to set up the following value.
            return view;
        }
        for(ProductVariant variant : variants){
            variantList.add(variant.getTitle());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item,variantList );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        SpinnerSelectedListener selectedListener = new SpinnerSelectedListener();
        spinner.setOnItemSelectedListener(selectedListener);

        Bitmap cachedImage = ThumbnailCache.get(mProduct.getImage().getSrc());
        if(cachedImage!= null) image.setImageBitmap(cachedImage);

        //enable back button on action bar
       // ( (AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return view;
    }


    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putParcelable(PRODUCT_DATA_KAY, mProduct);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null ){
            mProduct = savedInstanceState.getParcelable(PRODUCT_DATA_KAY);
        }

        new UpdateProductDataTask().execute(mProduct);

    }

    public static void setTaskCompleteListener(UpdateTaskResultListener listener){
        taskCompleteListener = listener;
    }


    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int pos, long id) {
            List<ProductVariant> variants = mProduct.getVariants();
            if(variants != null){
                ProductVariant variant = variants.get(pos);
                vendor.setText(mProduct.getVendor());
                price.setText(String.valueOf(variant.getPrice()));
                weight.setText(String.valueOf(variant.getWeight()));
                weightUnit.setText(variant.getWeight_unit());
                quantity.setText(String.valueOf(variant.getInventory_quantity()));
                taxable.setText(String.valueOf(variant.isTaxable()));
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }

    private class UpdateProductDataTask extends AsyncTask<Product, Void, Boolean> {

        private final static String UpdateProductDataTask_TAG = "UpdateProductDataTask";
        protected Boolean doInBackground(Product... products) {
        /*first we get the latest product info from the server*/
            Log.i(UpdateProductDataTask_TAG, "UpdateProductDataTask created");
            int count = products.length;
            if (count != 1) {
                Log.e(UpdateProductDataTask_TAG, "input product is more than 1, should not happen");
            }
            Product oldProduct = products[0];
            ShopifyApi mApi = RetrofitManager.getShopifyApi();
            final Call<Product> mCall = mApi.getProductById(String.valueOf(oldProduct.getId()),
                    getResources().getString(R.string.shopify_access_token));
            Product updatedProduct = null;
            try {
                updatedProduct = mCall.execute().body();
            } catch (Exception ex) {
                Log.e(UpdateProductDataTask_TAG, "UpdateProductData API request failed");
                ex.printStackTrace();
            }

            //now we compare the cached product with updatedProduct to see if we need update or not
            return updatedProduct != null && oldProduct.equals(updatedProduct);

        }

        protected void onPostExecute(Boolean result) {
            Log.v(UpdateProductDataTask_TAG, "the product need update: " + result);
            taskCompleteListener.onTaskResultReceived(result);
        }


    }

}

 interface UpdateTaskResultListener {
     void onTaskResultReceived(Boolean result);
}


