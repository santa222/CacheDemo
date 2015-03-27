package com.test.mytest;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.test.mytest.network.RequestQueueSingleton;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class TestApp extends Application {
    private static TestApp mInstance;

    public void onCreate() {
        //mRequestQueue = Volley.newRequestQueue(this);//this is a basic way
        mInstance = this;
        //init request queque as soon as app create
        RequestQueueSingleton.getInstance(this.getApplicationContext()).getRequestQueue();
    }

    public static synchronized TestApp getInstance() {
        return mInstance;
    }



}
