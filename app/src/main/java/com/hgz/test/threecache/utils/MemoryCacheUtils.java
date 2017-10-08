package com.hgz.test.threecache.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Administrator on 2017/9/26.
 */

public class MemoryCacheUtils {
    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static long maxMemory = Runtime.getRuntime().maxMemory();
    private static LruCache<String,Bitmap> memoryCache=new LruCache<String,Bitmap>((int) maxMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                int byteCount =value.getByteCount();// 获取图片占用内存大小
                return byteCount;
            }
        };
    /**
     * 向内存中写
     */
    public static void setBitmapToMemory(String url,Bitmap bitmap){
            memoryCache.put(url, bitmap);
    }
    /**
     * 从内存中读
     */
    public static Bitmap getBitmapFromMemory(String url){
        return memoryCache.get(url);
    }
}
