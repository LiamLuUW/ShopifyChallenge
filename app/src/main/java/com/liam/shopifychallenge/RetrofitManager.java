package com.liam.shopifychallenge;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * RetrofitManager is a global class handles all the retrofit logic
 */

public class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    private static final String baseURL = "https://shopicruit.myshopify.com/";
    private static final String SHOPIFY_API_KEY = "c32313df0d0ef512ca64d5b336a0d7c6";
    private static final String API_PARAMETER = "access_token";
    private static Retrofit retrofit;
    private static ShopifyApi shopifyApi;

    private RetrofitManager() {
    }

    ;


    public static void init() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add an interceptor to log detail server response
        // HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //httpClient.addInterceptor(logging);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter(API_PARAMETER, SHOPIFY_API_KEY)
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        Log.v(TAG, "retrofit manager instance created");
    }

    public static ShopifyApi getShopifyApi() {
        return (shopifyApi == null) ? createShopifyApi() : shopifyApi;
    }

    private static ShopifyApi createShopifyApi() {
        if (retrofit != null) {
            Log.v(TAG, "Shopify API instance created");
            shopifyApi = retrofit.create(ShopifyApi.class);
            return shopifyApi;
        } else {
            //error case
            Log.v(TAG, "Shopify API instance newInstance error");
            return null;
        }
    }
}



