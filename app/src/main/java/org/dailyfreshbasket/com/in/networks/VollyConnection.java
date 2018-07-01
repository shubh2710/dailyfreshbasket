package org.dailyfreshbasket.com.in.networks;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import org.dailyfreshbasket.com.in.MyApplication;


/**
 * Created by shubham on 2/20/2017.
 */

public class VollyConnection {
    private static VollyConnection sInstance=null;
    private RequestQueue mRequestQueue;
    private ImageLoader imageLoader;
    private VollyConnection(){

        mRequestQueue= Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader=new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private LruCache<String,Bitmap> cache=new LruCache<>((int)(Runtime.getRuntime().maxMemory()/1024)/8);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url,bitmap);
            }
        });
    }
    public static VollyConnection getsInstance(){
        if(sInstance==null){
            sInstance=new VollyConnection();
        }
        return sInstance;
    }
    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }
    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
