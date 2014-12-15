package com.test.mytest;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class VollyRequest {
    private static VollyRequest mInstance;

    private static RequestQueue mRequestQueue;
    private static boolean isCache;

    public static void setRequestQueue(RequestQueue mRequestQueue){
        VollyRequest.mRequestQueue=mRequestQueue;
    }

    public static synchronized VollyRequest getInstance() {
        if (mInstance == null) {
            mInstance = new VollyRequest();
        }
        return mInstance;
    }

    public static void setIsCache(boolean isCache){
        VollyRequest.isCache=isCache;
    }

    public void makeStringRequest(final int requestType,String url, final Map<String, String> params,
                                         Response.Listener successListener,Response.ErrorListener errorListener){
        if(requestType== Request.Method.GET&&isCache){
            Cache cache = TextApp.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(url);
            if(entry != null){
                try {
                    String data = new String(entry.data, "UTF-8");
                    // handle data, like converting it to xml, json, bitmap etc.,
                    Log.v("222","read from cache: "+data);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                readFromWebRequest(requestType,url,params,successListener,errorListener);
            }
        }else{
            // Cached response doesn't exists. Make network call here
            readFromWebRequest(requestType,url,params,successListener,errorListener);

        }
    }

    private void readFromWebRequest(final int requestType,String url,final Map<String, String> params,Response.Listener successListener,Response.ErrorListener errorListener){
        StringRequest stringRequest = new StringRequest(requestType,url,successListener, errorListener){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(requestType== Request.Method.GET) {//return null
                    super.getParams();
                }
                return params;
            }

            /**
             * Passing some request headers
             * */
           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("apiKey", "xxxxxxxxxxxxxxx");
                return headers;
            }*/
        };
        stringRequest.setShouldCache(isCache);
        mRequestQueue.add(stringRequest);
    }

    public void invalidateCache(String url){
        TextApp.getInstance().getRequestQueue().getCache().invalidate(url, true);
    }

    public void clearAllCache(){
        TextApp.getInstance().getRequestQueue().getCache().clear();
    }
    public void clearParticularCache(String url){
        TextApp.getInstance().getRequestQueue().getCache().remove(url);
    }









/*

    public static void makeJsonObjRequest(final int requestType,String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestType,url, requestType== Request.Method.GET?null:getJsonStringParams(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject responseObj) {
                        Log.d("TAG", responseObj.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }

    public static void makeJsonArrRequest(final int requestType,String url) {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(requestType,url, requestType== Request.Method.GET?null: getJsonStringParams(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseObj) {
                        Log.d("TAG", responseObj.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });

        mRequestQueue.add(jsonObjectRequest);
    }
    private static String getJsonStringParams(){
        String jsonStr=null;
        return jsonStr;
    }*/
}
