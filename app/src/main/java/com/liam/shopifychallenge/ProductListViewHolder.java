package com.liam.shopifychallenge;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Liam on 2018-01-03.
 */

public class ProductListViewHolder extends RecyclerView.ViewHolder {

    public ImageView productImage;
    public TextView productTitle;
    public TextView productDesc;

    public ProductListViewHolder(View view) {
        super(view);
        productImage = (ImageView)view.findViewById(R.id.product_image);
        productTitle = (TextView)view.findViewById(R.id.product_title);
        productDesc = (TextView)view.findViewById(R.id.product_description);
    }
}
