package com.liam.shopifychallenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

/**
 * Created by Liam on 2018-01-08.
 */

public class ProductDetailFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final static String TAG = "ProductDetailFragment";
    private final static String PRODUCT_DATA_KAY = "ProductDetailFragment.product";
    private Product mProduct;

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


    public static ProductDetailFragment create(Product product) {
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

        title = (TextView) view.findViewById(R.id.product_detail_title);
        description = (TextView) view.findViewById(R.id.product_detail_description);
        image = (ImageView) view.findViewById(R.id.product_detail_image);
        spinner = (Spinner) view.findViewById(R.id.product_detail_selection);
        vendor = (TextView) view.findViewById(R.id.product_detail_vendor);
        price = (TextView) view.findViewById(R.id.product_detail_price);
        weight = (TextView) view.findViewById(R.id.product_detail_weight);
        weightUnit = (TextView) view.findViewById(R.id.product_detail_weight_unit);
        quantity = (TextView) view.findViewById(R.id.product_detail_quantity);
        taxable = (TextView) view.findViewById(R.id.product_detail_taxable);

        title.setText(mProduct.getTitle());
        description.setText(mProduct.getBody_html());
        // set up spinner content
        List<ProductVariant> variants = mProduct.getVariants();
        List<String> variantList = new ArrayList<String>();
        if(variants == null) {
            // no need to set up the following value.
            return view;
        }
        for(ProductVariant variant : variants){
            variantList.add(variant.getTitle());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,variantList );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);




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

    }

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

/*class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
    protected Long doInBackground(URL... urls) {
        int count = urls.length;
        long totalSize = 0;
        for (int i = 0; i < count; i++) {
            totalSize += Downloader.downloadFile(urls[i]);
            publishProgress((int) ((i / (float) count) * 100));
            // Escape early if cancel() is called
            if (isCancelled()) break;
        }
        return totalSize;
    }

    protected void onProgressUpdate(Integer... progress) {
        setProgressPercent(progress[0]);
    }

    protected void onPostExecute(Long result) {
        showDialog("Downloaded " + result + " bytes");
    }
}*/
