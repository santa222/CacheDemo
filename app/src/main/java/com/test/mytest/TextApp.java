package com.test.mytest;

import android.app.Application;

import com.android.volley.RequestQueue;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class TextApp extends Application {
    private static TextApp mInstance;

    public void onCreate() {
        //mRequestQueue = Volley.newRequestQueue(this);//this is a basic way
        mInstance = this;
        RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    public static synchronized TextApp getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue(){
        return RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

}
