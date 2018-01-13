package com.liam.shopifychallenge;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * RAM Cache used for storing temporary downloaded thumbnails. Global level.
 */

public class ThumbnailCache {
    private static final String THUMBNAIL_CACHE_TAG = "ThumbnailCache";
    private static final int MAX_CACHE_LIMIT = 100; //limit for number of items in cache
    private static final int MIN_CACHE_LIMIT = 10;

    private static LinkedHashMap<String, Bitmap> mCache;

    private ThumbnailCache(){
        mCache = new LinkedHashMap<>(0,0.75f, true);
        Log.i(THUMBNAIL_CACHE_TAG, "cache created");
    }

    public static ThumbnailCache create(){
        return new ThumbnailCache();
    }

    public static void put(@NonNull final String key, @NonNull final Bitmap content){
        mCache.put(key,content);
        if(mCache.size() > MAX_CACHE_LIMIT){
            trimCacheSize(MIN_CACHE_LIMIT);//resize cache to 10 items
        }
    }

    public static Bitmap get(@NonNull final String key){
        try{
            if(mCache.containsKey(key)){
                return mCache.get(key);
            }else{
                return null;
            }
        }catch (NullPointerException ex){
            Log.v(THUMBNAIL_CACHE_TAG,"cache does not exist");
            return null;
        }

    }

    public static void clear(){
        mCache.clear();
    }

    public static int getSize(){
        return mCache.size();
    }

    public static void trimCacheSize(final int newSize){
        Log.i(THUMBNAIL_CACHE_TAG,"Cache reached its Max capacity, trimming now...");
        final Iterator<String> it = mCache.keySet().iterator();
        while(it.hasNext() && mCache.size() > newSize){
            it.next();
            it.remove();
        }
        Log.i(THUMBNAIL_CACHE_TAG, "Cache trimming complete");
    }

}
