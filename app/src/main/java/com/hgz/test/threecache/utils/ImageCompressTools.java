package com.hgz.test.threecache.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/25.
 */

public class ImageCompressTools {
    public static int caculateSampleSize(BitmapFactory.Options options,int reqWidth,int reqHeight){
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int simpleSize=1;
        if (outWidth>reqWidth||outHeight>reqHeight){
            int halfWidth = outWidth / 2;
            int halfHeight = outHeight / 2;
            while ((halfWidth/simpleSize)>=reqWidth&&(halfHeight/simpleSize)>=reqHeight){
                simpleSize*=2;
            }
        }
        return simpleSize;
    }
    public static Bitmap decodeStreamFromNetWork(URL url, InputStream is, int reqWidth, int reqHeight) throws Exception{

        BitmapFactory.Options options = new BitmapFactory.Options();
        //假解析
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,options);

        //计算采样率
        options.inSampleSize = caculateSampleSize(options,reqWidth,reqHeight);
        //开始真正解析图片
        options.inJustDecodeBounds = false;
        //关闭流,避免decodeStream(1,2,3);bug
        is.close();
        InputStream inputStream = url.openStream();
        return BitmapFactory.decodeStream(inputStream,null,options);
    }
}
