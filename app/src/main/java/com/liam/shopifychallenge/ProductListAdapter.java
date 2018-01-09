package com.liam.shopifychallenge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Liam on 2018-01-03.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {

    private List<Product> productList;
    private Context mContext;

    public ProductListAdapter(Context context){
        mContext = context;
    }

    public void setProductList(List<Product> productList){
        this.productList = productList;
    }

    @Override
    public void onBindViewHolder(final ProductListViewHolder viewHolder, int position){
        final Product product = productList.get(position);
        final String productTitle = product.getTitle();
        final String productDesc = product.getBody_html();
        final String imageSrc;
        if(product.getImage() != null){
            imageSrc = product.getImage().getSrc();
            //Using Picasso is pretty much the same performance as my own so we will see...
            //Picasso.with(mContext).load(imageSrc).error(R.drawable.default_product).placeholder(R.drawable.default_product).into(viewHolder.productImage);
            ThumbnailManager.getThumbnail(imageSrc, viewHolder);
        }


        viewHolder.productTitle.setText(productTitle);
        viewHolder.productDesc.setText(productDesc);

    }

    @Override
    public ProductListViewHolder onCreateViewHolder(ViewGroup viewGroup, int position){
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item_view, viewGroup, false);
        return new ProductListViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return (productList == null)? 0 : productList.size();
    }

}
