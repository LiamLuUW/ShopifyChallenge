package com.liam.shopifychallenge;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ViewHolder for productList RecyclerView
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private OnProductClickListener onProductClickListener;

    public ImageView productImage;
    public TextView productTitle;
    public TextView productQuantity;
    public TextView productDesc;

    public ProductListViewHolder(View view, OnProductClickListener onProductClickListener) {
        super(view);
        this.onProductClickListener = onProductClickListener;
        productImage = view.findViewById(R.id.product_image);
        productTitle = view.findViewById(R.id.product_title);
        productDesc = view.findViewById(R.id.product_description);
        productQuantity = view.findViewById(R.id.product_total_quantity);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onProductClickListener.onItemClick(view, getAdapterPosition());
    }
}
