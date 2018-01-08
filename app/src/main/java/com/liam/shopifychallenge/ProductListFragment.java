package com.liam.shopifychallenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
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
    private ProductListAdapter productListAdapter;
    private ProductList productList;
    private SwipeRefreshLayout swipeLayout;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        Log.i(TAG, "ProductListFragment created");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance){
        final View view = inflater.inflate(R.layout.product_list_view, viewGroup, false);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.product_list_swipe_container);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                displayTrueData();
            }
        });

        productListAdapter = new ProductListAdapter();
        productList = (ProductList) view.findViewById(R.id.product_list_view);
        productList.setAdapter(productListAdapter);
        /*productList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView,
                                             int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx,
                                   int dy) {
                swipeLayout.setEnabled(productList.getLinearLayoutManager().findFirstCompletelyVisibleItemPosition()==0);
            }
        });*/



        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        displayMockData();
    }

    private void displayMockData(){
        List<Product> mockList = new ArrayList<>();
        Product p1 = new Product(123, "p1", "this is p1");
        mockList.add(p1);

        Product p2 = new Product(123, "p2", "this is p2");
        mockList.add(p2);

        Product p3 = new Product(123, "p3", "this is p3");
        mockList.add(p3);

        Product p4 = new Product(123, "p4", "this is p4");
        mockList.add(p4);

        Product p5 = new Product(123, "p5", "this is p5");
        mockList.add(p5);

        Product p6 = new Product(123, "p6", "this is p6");
        mockList.add(p6);

        productListAdapter.setProductList(mockList);
        productListAdapter.notifyDataSetChanged();



    }

    private void displayTrueData(){
        Log.i(TAG, "Load products data from server");
        ShopifyApi mApi = RetrofitManager.getShopifyApi();
        Call<ProductListResponse> call = mApi.getProductList(1, "c32313df0d0ef512ca64d5b336a0d7c6");

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse mResponse = response.body();
                if(mResponse == null){
                    Log.e(TAG, "Error: empty response body received, just ignore");
                    return;
                }
                Log.v(TAG, "Products response received from server with list size of "
                        + mResponse.products.size());
                productListAdapter.setProductList(mResponse.products);
                productListAdapter.notifyDataSetChanged();
                swipeLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                Log.e(TAG, "Error: Products response onFailure");
            }
        });

    }


}
