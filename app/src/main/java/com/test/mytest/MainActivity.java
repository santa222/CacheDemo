package com.test.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private static String testUrl="http://www.myurl.com";//contain the header
    private static String jsonArrayTestUrl="http://api.androidhive.info/volley/person_array.json";//not contain the header
    private static String jsonObjectTestUrl="http://api.androidhive.info/volley/person_object.json";

    private RequestQueue mRequestQueue;


    /**
     * Volley decides whether to cache response or not based only on headers
     * "Cache-Control" and then "Expires", "maxAge".
     *
     * if the header does not contain those mentioned above
     * the isCache variable would take effect
     */
    private boolean isCache=true;//change this to see cache effect(is)

    Response.Listener successListener= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("TAG", response);
            Log.d("TAG", "getResponseSuccess");
        }
    };
    Response.ErrorListener errorListener=new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("TAG", error.getMessage(), error);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VollyRequest.setIsCache(isCache);

        //mRequestQueue = Volley.newRequestQueue(this);//this is a basic way
        mRequestQueue = RequestQueueSingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VollyRequest.getInstance().makeStringRequest(Request.Method.GET,jsonArrayTestUrl,makeCustomParams(),successListener,errorListener);
               // VollyRequest.makeJsonObjRequest(Request.Method.GET);
                //VollyRequest.makeJsonArrRequest(Request.Method.GET, jsonArrayTestUrl);
            }
        });
    }

    //
    private Map<String, String> makeCustomParams(){
        Map<String, String> map = new HashMap<>();
        map.put("params1", "value1");
        map.put("params2", "value2");
        return map;
    }


    @Override
    protected void onStop () {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);//TAG is available for cancelAll() method
        }
    }

}