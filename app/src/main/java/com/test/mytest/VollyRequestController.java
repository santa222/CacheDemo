package com.test.mytest;


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class VollyRequestController {
    private static VollyRequestController mInstance;

    private static RequestQueue mRequestQueue;
    private static boolean isCache;

    public static void setRequestQueue(RequestQueue mRequestQueue){
        VollyRequestController.mRequestQueue=mRequestQueue;
    }

    public static synchronized VollyRequestController getInstance() {
        if (mInstance == null) {
            mInstance = new VollyRequestController();
        }
        return mInstance;
    }

    public static void setIsCache(boolean isCache){
        VollyRequestController.isCache=isCache;
    }

   // String lastEtag,cachedData;
    public void makeStringRequest(final int requestType,String url, final Map<String, String> params,
                                         Response.Listener successListener,Response.ErrorListener errorListener){
        //if(requestType== Request.Method.GET&&isCache){
            Cache cache = TextApp.getInstance().getRequestQueue().getCache();
            Cache.Entry entry = cache.get(url);

            /*if(entry != null){
                try {
                    cachedData = new String(entry.data, "UTF-8");
                    // handle data, like converting it to xml, json, bitmap etc.,
                    //Log.v("222","read from cache: "+data);
                    lastEtag=entry.etag;
                    Log.v("222","read from cache --Etag: "+lastEtag);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else{
                readFromWebRequest(requestType,url,params,successListener,errorListener);
            }*/
        //}else{
            // Cached response doesn't exists. Make network call here
            readFromWebRequest(requestType, url, params, successListener, errorListener, entry);

        //}
    }

    private void readFromWebRequest(final int requestType,String url,final Map<String, String> params,Response.Listener successListener,Response.ErrorListener errorListener, final Cache.Entry entry){
        VolleyRequest stringRequest = new VolleyRequest(requestType,url,successListener, errorListener,entry){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(requestType== Request.Method.GET) {//return null
                    super.getParams();
                }
                return params;
            }

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                //headers.put("apiKey", "xxxxxxxxxxxxxxx");
                headers.put("Accept", "application/json");
                if(entry!=null*//*&&entry.etag!=null*//*)
                    headers.put("If-None-Match", entry.etag);

                Log.v("222","----send head----- ");
                for(String key:headers.keySet()){
                    Log.v("222","key: "+key+" , "+headers.get(key));
                }
                Log.v("222","----------------- ");
                return headers;
            }*/
        };
        Log.v("222","before added to queue");
        stringRequest.setShouldCache(true);
        mRequestQueue.add(stringRequest);


    }

}
