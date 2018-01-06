package com.liam.shopifychallenge;

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
    private static final String baseURL = "https://shopicruit.myshopify.com/";
    private static Retrofit retrofit;
    private static ShopifyApi shopifyApi;

    private RetrofitManager(){};


    public static void init(){

        // add an interceptor to append access token for each request

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        /*OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("access_token", "c32313df0d0ef512ca64d5b336a0d7c6")
                                .build();

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                }).build();*/

        retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static ShopifyApi getShopifyApi(){
        return (shopifyApi == null) ? createShopifyApi() : shopifyApi;
    }

    private static ShopifyApi createShopifyApi(){
        if(retrofit != null){
            shopifyApi = retrofit.create(ShopifyApi.class);
            return shopifyApi;
        }else{
            //error case
            return null;
        }
    }
}



