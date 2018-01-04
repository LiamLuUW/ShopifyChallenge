package com.liam.shopifychallenge;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.Collection;

/**
 * Created by Liam on 2018-01-03.
 */

public class ProductList extends RecyclerView {

    private final LinearLayoutManager linearLayoutManager;

    public ProductList(final Context context, final AttributeSet attrs){
        super(context, attrs);
        linearLayoutManager = new LinearLayoutManager(context);
        setHasFixedSize(true);
        setLayoutManager(linearLayoutManager);
    }

    public void setProducts(Collection<Product> products){
        if(getAdapter() instanceof ProductListAdapter) {
            final ProductListAdapter adapter = (ProductListAdapter) getAdapter();
            adapter.setItems(products);
            adapter.notifyDataSetChnag
        }
    }
}
