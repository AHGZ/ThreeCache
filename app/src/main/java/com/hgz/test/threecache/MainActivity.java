package com.hgz.test.threecache;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.hgz.test.threecache.utils.ImageCompressTools;
import com.hgz.test.threecache.utils.LocalCacheUtils;
import com.hgz.test.threecache.utils.MemoryCacheUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private String imagePath = "http://t2.27270.com/uploads/tu/201709/9999/6a9bcfad61.jpg";
//    private MemoryCacheUtils memoryCacheUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        memoryCacheUtils = new MemoryCacheUtils();
        image = (ImageView) findViewById(R.id.show_image);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImage();
            }
        });
    }

    private void getImage() {
        new AsyncTask<String, Void, Bitmap>() {
            //Params:放置请求链接地址以及请求参数类型
            //Progress:进度，当你们下载文件的时候，Integer
            //Result:限定你所请求网络数据返回的类型是什么，String
            @Override
            protected Bitmap doInBackground(String... params) {
                try {
                    String path = params[0];
                    MemoryCacheUtils memoryCacheUtils = new MemoryCacheUtils();
                    Bitmap bitmapFromMemory = memoryCacheUtils.getBitmapFromMemory(imagePath);
                    if (bitmapFromMemory!=null){
                        System.out.println("=============================加载的是内存图片");
                        return  bitmapFromMemory;
                    }
                    Bitmap bitmapFromLocal = LocalCacheUtils.getBitmapFromLocal(imagePath);
                    if (bitmapFromLocal!=null){
                        System.out.println("=============================加载的是本地图片");
                        return bitmapFromLocal;
                    }
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    connection.setConnectTimeout(5000);
                    int code = connection.getResponseCode();
                    if (code == 200) {
                        InputStream is = connection.getInputStream();
                        System.out.println("=============================加载的是网络图片");
                        return ImageCompressTools.decodeStreamFromNetWork(url, is, 50, 50);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                if (bitmap != null) {
                    setBitmapToCacheAndMemory(imagePath, bitmap);
                    image.setImageBitmap(bitmap);
                } else {
                    image.setImageResource(R.mipmap.ic_launcher);
                }
            }
        }.execute(imagePath);
    }
    //向内存与本地添加的方法
    public void setBitmapToCacheAndMemory(String url, Bitmap bitmap) {
        //内存
        MemoryCacheUtils.setBitmapToMemory(url, bitmap);
        //本地
        LocalCacheUtils.setBitmapToLocal(url, bitmap);
    }
}
