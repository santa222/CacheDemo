package com.test.mytest;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class RequestQueueSingleton {

    private static RequestQueueSingleton mInstance;
    private String userAgent = "volley/0";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private RequestQueueSingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,new LruBitmapCache());
    }

    public static synchronized RequestQueueSingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            HttpStack stack;
            // If the device is running a version >= Gingerbread...
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                // ...use HttpURLConnection for stack.
                stack=new HurlStack();
            } else {
                // ...use HttpClient for stack.
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
            Network network = new BasicNetwork(stack);


            // Instantiate the cache
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 1024 * 1024); // 1MB cap


            // Instantiate the RequestQueue with the cache and network.
            mRequestQueue = new RequestQueue(cache, network);

            // Start the queue
            mRequestQueue.start();

            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        VollyRequestController.setRequestQueue(mRequestQueue);
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
        Log.v("222", "added to queue");

    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? null : tag);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public void invalidateCache(String url){
        mRequestQueue.getCache().invalidate(url, true);
    }

    public void clearAllCache(){
        mRequestQueue.getCache().clear();
    }
    public void clearParticularCache(String url){
        mRequestQueue.getCache().remove(url);
    }

}
