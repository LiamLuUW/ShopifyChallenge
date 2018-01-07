package com.liam.shopifychallenge;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Liam on 2018-01-04.
 */

public interface ShopifyApi {

    /**
     * get request that calls shopofy api to retrieve a list of products
     */
    @GET("admin/products.json")
    Call<ProductListResponse> getProductList(@Query("page") int page, @Query("access_token") String accessToken);

    /**
     * get request for dynamic url to download image resource
     */
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);

}
