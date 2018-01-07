package com.liam.shopifychallenge;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private ProductListAdapter productListAdapter;
    private ProductList productList;

    @Override
    public void onCreate(final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstance){
        final View view = inflater.inflate(R.layout.product_list_view, viewGroup, false);
        productListAdapter = new ProductListAdapter();
        productList = (ProductList) view.findViewById(R.id.product_list_view);
        productList.setAdapter(productListAdapter);
        //displayMockData();
        //displayTrueData();
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        displayTrueData();
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
        ShopifyApi mApi = RetrofitManager.getShopifyApi();
        ProductListResponse mresponse = new ProductListResponse();
        Call<ProductListResponse> call = mApi.getProductList(1, "c32313df0d0ef512ca64d5b336a0d7c6");

        call.enqueue(new Callback<ProductListResponse>() {
            @Override
            public void onResponse(Call<ProductListResponse> call, Response<ProductListResponse> response) {
                ProductListResponse mResponse = response.body();

                System.out.println("yes " + mResponse.products.size());
                //Product temp = mResponse.products.get(0);
                //Log.i("xxx",temp.getTitle() + " " + temp.getImage().getSrc());
                productListAdapter.setProductList(mResponse.products);
                productListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                System.out.println("no server side error");
            }
        });

    }

    private void getProductThumbnail(){

    }

}
