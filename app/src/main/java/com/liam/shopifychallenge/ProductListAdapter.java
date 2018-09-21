package com.liam.shopifychallenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Productlist Adapter for the productlist recyclerView
 */

class ProductListAdapter extends RecyclerView.Adapter<ProductListViewHolder> {

    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    ProductListAdapter(OnProductClickListener onProductClickListener) {
        this.onProductClickListener = onProductClickListener;
    }

    void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public void onBindViewHolder(final ProductListViewHolder viewHolder, int position) {
        final Product product = productList.get(position);
        final String productTitle = product.getTitle();
        final String productDesc = product.getBody_html();
        final int amount = product.getTotalQuantity();
        final String imageSrc;
        if (product.getImage() != null) {
            imageSrc = product.getImage().getSrc();
            ThumbnailManager.getThumbnail(imageSrc, viewHolder);
        }
        viewHolder.productTitle.setText(productTitle);
        viewHolder.productDesc.setText(productDesc);
        viewHolder.productQuantity.setText(String.valueOf(amount));

    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_item_view, viewGroup, false);
        return new ProductListViewHolder(view, onProductClickListener);
    }

    @Override
    public int getItemCount() {
        return (productList == null) ? 0 : productList.size();
    }

    Product getProductByPosition(int pos) {
        return productList.get(pos);
    }

}
