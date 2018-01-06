package com.liam.shopifychallenge;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Liam on 2018-01-04.
 */

public interface ShopifyApi {

    /**
     * get request that calls shopofy api to retrieve a list of products
     */
    @GET("admin/products.json")
    Call<ProductListResponse> getProductList(@Query("page") int page, @Query("access_token") String accessToken);

}
