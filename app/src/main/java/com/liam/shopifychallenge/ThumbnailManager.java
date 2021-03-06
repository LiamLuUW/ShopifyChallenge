package com.liam.shopifychallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Global level class handles all thumbnail logic
 */

public class ThumbnailManager {

    private final static String TAG = ThumbnailManager.class.getSimpleName();
    private final static int MAC_THREAD_CAP = 5;

    // default image show in list (Before online image download)
    private final static int default_image = R.drawable.default_product;


    private static ExecutorService executorService;
    private static Handler mHandler;

    private ThumbnailManager() {
    }

    public static void init() {
        Log.i(TAG, "Thumbnail manager instance created");
        ThumbnailCache.create();
        // a fixed size thread pool to download images
        executorService = Executors.newFixedThreadPool(MAC_THREAD_CAP);
        mHandler = new Handler(); // handler for main UI thread
    }

    public static void getThumbnail(String url, ProductListViewHolder viewHolder) {
        //first we check the thumbnail is in the cache or not
        Bitmap bitmap = ThumbnailCache.get(url);

        if (bitmap != null) {
            Log.v(TAG, "load thumbnail from cache");
            viewHolder.productImage.setImageBitmap(bitmap);
        } else {
            Log.v(TAG, "load thumbnail from server");
            executorService.submit(new DownloadRunnable
                    (new Pair<String, ProductListViewHolder>(url, viewHolder)));
        }
    }


    private static class DownloadRunnable implements Runnable {
        private final static String TAG_DOWNLOAD_RUNNABLE = "DownloadRunnable";
        private final Pair<String, ProductListViewHolder> info;

        public DownloadRunnable(Pair<String, ProductListViewHolder> info) {
            Log.v(TAG_DOWNLOAD_RUNNABLE, "download image runnable task created");
            this.info = info;
        }

        @Override
        public void run() {
            Bitmap bitmap = downloadThumbnail(info.first);

            if (bitmap != null) {
                //insert into cache
                ThumbnailCache.put(info.first, bitmap);
            }

            DisplayThumbnail bd = new DisplayThumbnail(bitmap, info);

            //notify main thread to post updating the downloaded thumbnail
            Log.v(TAG_DOWNLOAD_RUNNABLE, "update UI thread for ready thumbnail");
            mHandler.post(bd);
        }

        private Bitmap downloadThumbnail(String url) {
            ShopifyApi mApi = RetrofitManager.getShopifyApi();
            Call<ResponseBody> call = mApi.downloadImageFromUrl(url);
            Response<ResponseBody> response = null;
            Bitmap mThumbnail = null;
            //since this is already on a separate thread, no need to enqueue anymore, just execute
            try {
                Log.v(TAG_DOWNLOAD_RUNNABLE, "Start downloading and decoding thumbnail");
                response = call.execute();
            } catch (Exception ex) {
                Log.e(TAG_DOWNLOAD_RUNNABLE, "Error: onResponse for downloading thumbnail from server");
            }

            if (response != null && response.isSuccessful()) {
                try {
                    mThumbnail = BitmapFactory.decodeStream(response.body().byteStream());
                } catch (Exception ex) {
                    Log.e(TAG_DOWNLOAD_RUNNABLE, "Error: onDecode thumbnail");
                    ex.printStackTrace();
                }
            }

            Log.v(TAG_DOWNLOAD_RUNNABLE, "thumbnail downloaded and decoded successfully");
            mThumbnail = getResizedBitmap(mThumbnail,120);
            return mThumbnail;
        }

        private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
            int width = image.getWidth();
            int height = image.getHeight();

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }

    }

    private static class DisplayThumbnail implements Runnable {
        private final static String TAG_DISPLAY_THUMBNAIL = "DisplayThumbnail";
        Bitmap bitmap;
        Pair<String, ProductListViewHolder> info;

        public DisplayThumbnail(Bitmap bitmap, Pair<String, ProductListViewHolder> info) {
            Log.v(TAG_DISPLAY_THUMBNAIL, "display thumbnail task created");
            this.bitmap = bitmap;
            this.info = info;
        }

        public void run() {
            // Show bitmap on UI
            Log.v(TAG_DISPLAY_THUMBNAIL, "displaying thumbnail on main thread");
            if (bitmap != null) {
                info.second.productImage.setImageBitmap(bitmap);
            } else {
                info.second.productImage.setImageResource(default_image);
            }
        }
    }

    public static void clearCache() {
        Log.v(TAG, "clear all caches");
        ThumbnailCache.clear();
    }

}


