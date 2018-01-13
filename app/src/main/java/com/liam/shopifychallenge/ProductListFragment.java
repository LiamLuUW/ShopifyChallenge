package com.liam.shopifychallenge;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Liam on 2018-01-03. This fragment hold the product list view
 */

public class ProductListFragment extends Fragment{
    private final static String TAG = "ProductListFragment";
    private final static int REQUEST_PAGE_NUMBER = 1;
    private boolean needUpdate;
    private ProductListAdapter productListAdapter;
    private ProductList productList;
    private List<Product> products;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        Log.i(TAG, "ProductListFragment created");
        super.onCreate(savedInstanceState);
        this.needUpdate = true;
        products = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup,
                             @Nullable Bundle savedInstance){
        final View view = inflater.inflate(R.layout.product_list_view, viewGroup, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.product_list_swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayTrueData();
            }
        });

        productListAdapter = new ProductListAdapter(createOnProductClickListener());
        productList = (ProductList) view.findViewById(R.id.product_list_view);
        productList.setAdapter(productListAdapter);

        //disable back button
        ( (AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        return view;
    }

    @Override
    public void onResume(){

        super.onResume();
        if(needUpdate || products== null){
            displayTrueData();
        }else{
            productListAdapter.setProductList(products);
            productListAdapter.notifyDataSetChanged();
        }
    }

    private OnProductClickListener createOnProductClickListener(){
        return new OnProductClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Product selectedProduct = productListAdapter.getProductByPosition(pos);
                ProductDetailFragment detailFragment = ProductDetailFragment.create(selectedProduct);
                UpdateTaskResultListener taskResultListener = new UpdateTaskResultListener() {
                    @Override
                    public void onTaskResultReceived(Boolean result) {
                        if(result) needUpdate = true;
                    }
                };
                ProductDetailFragment.setTaskCompleteListener(taskResultListener);
                needUpdate = false;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, detailFragment).addToBackStack(null);
                ft.commit();
            }
        };
    }

    private void displayTrueData(){
        Log.i(TAG, "Load products data from server");
        ShopifyApi mApi = RetrofitManager.getShopifyApi();
        Call<ProductListResponse> call = mApi.getProductList(REQUEST_PAGE_NUMBER,
                getResources().getString(R.string.shopify_access_token));

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ProductListResponse> call,
                                   @NonNull Response<ProductListResponse> response) {
                ProductListResponse mResponse = response.body();
                if(mResponse == null){
                    Log.e(TAG, "Error: empty response body received, just ignore");
                    return;
                }
                Log.v(TAG, "Products response received from server with list size of "
                        + mResponse.products.size());
                products = mResponse.products;
                productListAdapter.setProductList(products);
                productListAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ProductListResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "Error: Products response onFailure");
            }
        });

    }

}
