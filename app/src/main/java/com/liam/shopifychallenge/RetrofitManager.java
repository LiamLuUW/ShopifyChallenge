package com.liam.shopifychallenge;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Liam on 2018-01-04.
 */

public class RetrofitManager {
    private static final String TAG = "RetrofitManager";
    private static final String baseURL = "https://shopicruit.myshopify.com/";
    private static Retrofit retrofit;
    private static ShopifyApi shopifyApi;

    private RetrofitManager(){};


    public static void init(){

        // add an interceptor to log detail server response
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                //.client(httpClient.build())
                .build();

        Log.i(TAG, "retrofit manager instance created");
    }

    public static ShopifyApi getShopifyApi(){
        return (shopifyApi == null) ? createShopifyApi() : shopifyApi;
    }

    private static ShopifyApi createShopifyApi(){
        if(retrofit != null){
            Log.i(TAG, "Shopify API instance created");
            shopifyApi = retrofit.create(ShopifyApi.class);
            return shopifyApi;
        }else{
            //error case
            Log.i(TAG, "Shopify API instance create error");
            return null;
        }
    }
}



