package com.liam.shopifychallenge;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

        productListAdapter = new ProductListAdapter(getActivity().getApplicationContext(), createOnProductClickListener());
        productList = (ProductList) view.findViewById(R.id.product_list_view);
        productList.setAdapter(productListAdapter);


        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        displayTrueData();
    }

    private OnProductClickListener createOnProductClickListener(){
        return new OnProductClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Toast toast = Toast.makeText(getActivity(), "open new fragment now", Toast.LENGTH_SHORT);
                toast.show();
                Product selectedProduct = productListAdapter.getProductByPosition(pos);
                ProductDetailFragment detailFragment = ProductDetailFragment.create(selectedProduct);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_container, detailFragment).addToBackStack(null);
                ft.commit();
            }
        };
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

        Product p7 = new Product(123, "p7", "this is p7");
        mockList.add(p7);

        Product p8 = new Product(123, "p8", "this is p8");
        mockList.add(p8);

        Product p9 = new Product(123, "p9", "this is p9");
        mockList.add(p9);

        Product p10 = new Product(123, "p10", "this is p10");
        mockList.add(p10);

        Product p11 = new Product(123, "p11", "this is p11");
        mockList.add(p11);

        Product p12 = new Product(123, "p12", "this is p12");
        mockList.add(p12);

        Product p13 = new Product(123, "p13", "this is p13");
        mockList.add(p13);

        Product p14 = new Product(123, "p14", "this is p14");
        mockList.add(p4);

        Product p15 = new Product(123, "p15", "this is p15");
        mockList.add(p5);

        Product p16 = new Product(123, "p16", "this is p16");
        mockList.add(p6);

        Product p17 = new Product(123, "p17", "this is p17");
        mockList.add(p17);

        Product p18 = new Product(123, "p18", "this is p18");
        mockList.add(p18);

        Product p19 = new Product(123, "p19", "this is p19");
        mockList.add(p19);

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
