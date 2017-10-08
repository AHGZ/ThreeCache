package com.hgz.test.threecache.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2017/9/25.
 */

public class LocalCacheUtils {
    public static final String CACHE_PATH= Environment.getExternalStorageDirectory()+"/local_cache";
    //向本地中存储
    public static void setBitmapToLocal(String url,Bitmap bitmap){
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH,fileName);
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {// 如果文件夹不存在, 创建文件夹
                parentFile.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.flush();
//            图片转换为输出流
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //从本地中读取
    public static Bitmap getBitmapFromLocal(String url){

        try {

            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH,fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
