package com.liam.shopifychallenge;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
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

    private final static String TAG = "ThumbnailManager";

    @NonNull
    private static ThumbnailCache thumbnailCache;

    @NonNull
    private static HashMap<String, ProductListViewHolder> thumbnailDownloads;

    // default image show in list (Before online image download)
    public final static int default_image = R.drawable.default_product;


    private static ExecutorService executorService;
    private static Handler mHandler;

    private ThumbnailManager() {
    }

    public static void init() {
        Log.i(TAG, "Thumbnail manager instance created");
        thumbnailCache = new ThumbnailCache();
        thumbnailDownloads = new HashMap<>();
        executorService = Executors.newFixedThreadPool(5); // a fixed size thread pool to download images
        mHandler = new Handler(); // handler for main UI thread
    }

    public static void getThumbnail(String url, ProductListViewHolder viewHolder) {
        //store url and viewholder into the queue
        thumbnailDownloads.put(url, viewHolder);
        //first we check the thumbnail is in the cache or not
        Bitmap bitmap = thumbnailCache.get(url);

        if (bitmap != null) {
            Log.v(TAG, "load thumbnail from cache");
            viewHolder.productImage.setImageBitmap(bitmap);
        } else {
            Log.v(TAG, "load thumbnail from server");
            executorService.submit(new DownloadRunnable
                    (new Pair<String, ProductListViewHolder>(url, viewHolder)));
        }
    }


    static class DownloadRunnable implements Runnable {
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
                thumbnailCache.put(info.first, bitmap);
            }

            DisplayThumbnail bd = new DisplayThumbnail(bitmap, info);

            //notify main thread to post updating the downloaded thumbnail
            Log.v(TAG_DOWNLOAD_RUNNABLE, "update UI thread for ready thumbnail");
            mHandler.post(bd);
        }

        private Bitmap downloadThumbnail(String url) {
            ShopifyApi mApi = RetrofitManager.getShopifyApi();
            Call<ResponseBody> call = mApi.downloadFileWithDynamicUrlSync(url);
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

    static class DisplayThumbnail implements Runnable {
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
        thumbnailCache.clear();
    }

}


