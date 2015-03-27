package com.test.mytest;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.test.mytest.network.RequestQueueSingleton;
import com.test.mytest.network.VollyRequestUtils;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    //private static String testUrl="https://edgepeek.com/categories.json";//contain the header
    private static String jsonArrayTestUrl="http://api.androidhive.info/volley/person_array.json";//not contain the header
    private static String jsonObjectTestUrl="http://api.androidhive.info/volley/person_object.json";

    private RequestQueue mRequestQueue;


 //   MatchTextView mMatchTextView;

    /**
     * Volley decides whether to cache response or not based only on headers
     * "Cache-Control" and then "Expires", "maxAge".
     *
     * if the header does not contain those mentioned above
     * the isCache variable would take effect
     */
    private boolean isCache=false;//change this to see cache effect(is)

    Response.Listener successListener= new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Log.d("TAG", response);
            Log.d("222", "getResponseSuccess");
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

        VollyRequestUtils.setIsCache(isCache);

        mRequestQueue = VollyRequestUtils.getRequestQueue();
        VollyRequestUtils.clearAllCache();

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VollyRequestUtils.makeStringRequest(Request.Method.GET, jsonArrayTestUrl, makeCustomParams(), successListener, errorListener);
            }
        });

      /*  mMatchTextView=(MatchTextView)findViewById(R.id.match_text);
        playAnimation(40);*/
    }

    //set the params(put or post)
    private Map<String, String> makeCustomParams(){
        Map<String, String> map = new HashMap<>();
        /*map.put("params1", "value1");
        map.put("params2", "value2");*/
        return map;
    }
/*

    private void playAnimation(int progress){
        mMatchTextView.setProgress(progress * 1f / 100);
    }

*/


    @Override
    protected void onStop () {
        super.onStop();

        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);//TAG is available for cancelAll() method
        }
    }

}
