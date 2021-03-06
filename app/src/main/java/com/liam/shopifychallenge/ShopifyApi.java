package com.liam.shopifychallenge;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * All Shopify APIs for Retrofit
 */

public interface ShopifyApi {

    /**
     * GET request that calls shopofy api to retrieve a list of products
     */
    @GET("admin/products.json")
    Call<ProductListResponse> getProductList(@Query("page") int page);

    /**
     * GET request for dynamic url to download image resource
     */
    @GET
    Call<ResponseBody> downloadImageFromUrl(@Url String fileUrl);

    /**
     * GET request for retrieve a single product details
     */
    @GET("admin/products/{product_id}.json?")
    Call<Product> getProductById(@Path("product_id") String productId);

}
