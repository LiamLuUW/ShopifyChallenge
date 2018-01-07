package com.liam.shopifychallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Liam on 2018-01-06.
 */

public class ThumbnailManager {

    @NonNull
    final ThumbnailCache thumbnailCache;

    @NonNull
    private HashMap<String, ProductListViewHolder> thumbnailDownloads;

    // default image show in list (Before online image download)
    final int default_image=R.drawable.default_product;


    ExecutorService executorService;
    Handler mHandler;

    public ThumbnailManager(){
        thumbnailCache = new ThumbnailCache();
        thumbnailDownloads = new HashMap<>();
        executorService = Executors.newFixedThreadPool(5);
        mHandler = new Handler();
    }

    public void getThumbnail(String url, ProductListViewHolder viewHolder){
        //store url and viewholder into the queue
        thumbnailDownloads.put(url, viewHolder);

        //first we check the thumbnail is in the cache or not
        Bitmap bitmap = thumbnailCache.get(url);

        if(bitmap != null){
            viewHolder.productImage.setImageBitmap(bitmap);
        }else{
            executorService.submit(new DownloadRunnable
                    (new Pair<String, ProductListViewHolder>(url, viewHolder)));
        }
    }


    class DownloadRunnable implements Runnable {
        private final Pair<String, ProductListViewHolder> info;

        public DownloadRunnable(Pair<String, ProductListViewHolder> info){
            this.info = info;
        }

        @Override
        public void run(){
            Bitmap bitmap = downloadThumbnail(info.first);

            if(bitmap != null){
                //insert into cache
                thumbnailCache.put(info.first, bitmap);
            }

            BitmapDisplayer bd=new BitmapDisplayer(bitmap, info);

            // Causes the Runnable bd (BitmapDisplayer) to be added to the message queue.
            // The runnable will be run on the thread to which this handler is attached.
            // BitmapDisplayer run method will call
            mHandler.post(bd);
        }

        private Bitmap downloadThumbnail(String url){
            ShopifyApi mApi = RetrofitManager.getShopifyApi();
            Call<ResponseBody> call = mApi.downloadFileWithDynamicUrlSync(url);
            Response<ResponseBody> response = null;
            Bitmap mThumbnail = null;
            //since this is already on a separate thread, no need to enqueue anymore, just execute
            try {
                response = call.execute();
            } catch (Exception ex) {
                Log.e("onResponse", "error");
            }

            if(response != null && response.isSuccessful()){
                try{
                    mThumbnail = BitmapFactory.decodeStream(response.body().byteStream());
                } catch (Exception ex){
                   Log.e("onDecode", "error");
                    ex.printStackTrace();
                }
            }

            return mThumbnail;
        }
    }

    class BitmapDisplayer implements Runnable{
        Bitmap bitmap;
        Pair<String, ProductListViewHolder> info;
        public BitmapDisplayer(Bitmap bitmap, Pair<String, ProductListViewHolder> info){
            this.bitmap = bitmap;
            this.info = info;
        }
        public void run()
        {
            // Show bitmap on UI
            if(bitmap!=null) {
                info.second.productImage.setImageBitmap(bitmap);
            } else {
                info.second.productImage.setImageResource(default_image);
            }
        }
    }

    public void clearCache(){
        thumbnailCache.clear();
    }

}


