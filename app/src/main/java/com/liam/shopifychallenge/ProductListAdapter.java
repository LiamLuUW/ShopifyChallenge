package com.liam.shopifychallenge;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Liam on 2018-01-03.
 */

public class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {

    private List<Product> productList;
    //private ThumbnailManager thumbnailManager = new ThumbnailManager();

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
