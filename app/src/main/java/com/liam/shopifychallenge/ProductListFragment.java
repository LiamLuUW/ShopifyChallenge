package com.liam.shopifychallenge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Liam on 2018-01-03. This fragment hold the product list view
 */

public class ProductListFragment extends Fragment {
    private final static String TAG = ProductListFragment.class.getSimpleName();
    private ProductListAdapter productListAdapter;
    private List<Product> products;
    private static UpdateTaskResultListener taskCompleteListener;


    public static ProductListFragment newInstance(ArrayList<Product> productList) {
        ProductListFragment productListFragment = new ProductListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(TAG, productList);
        productListFragment.setArguments(args);
        return productListFragment;
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        Log.i(TAG, "ProductListFragment created");
        super.onCreate(savedInstanceState);
        products = getArguments().getParcelableArrayList(TAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup,
                             @Nullable Bundle savedInstance) {
        final View view = inflater.inflate(R.layout.product_list_view, viewGroup, false);

        productListAdapter = new ProductListAdapter(createOnProductClickListener());
        ProductList productList = view.findViewById(R.id.product_list_view);
        productList.setAdapter(productListAdapter);
        if (products != null) {
            productListAdapter.setProductList(products);
            productListAdapter.notifyDataSetChanged();
        }

        //disable back button
        // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return view;
    }

    public static void setTaskCompleteListener(UpdateTaskResultListener listener) {
        taskCompleteListener = listener;
    }

    private OnProductClickListener createOnProductClickListener() {
        return new OnProductClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Product selectedProduct = productListAdapter.getProductByPosition(pos);
                ProductDetailFragment detailFragment = ProductDetailFragment.newInstance(selectedProduct);
                UpdateTaskResultListener taskResultListener =
                        result -> taskCompleteListener.onTaskResultReceived(result);
                ProductDetailFragment.setTaskCompleteListener(taskResultListener);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, detailFragment).addToBackStack(null);
                ft.commit();
            }
        };
    }

}
