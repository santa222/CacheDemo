package com.test.mytest.network;


import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.test.mytest.TestApp;
import com.test.mytest.network.request.VolleyRequest;

import java.util.Map;

/**
 * Created by mika_1990 on 14-12-15.
 */
public class VollyRequestUtils {
    private static boolean isCache;


    public static void setIsCache(boolean isCache){
        VollyRequestUtils.isCache=isCache;
    }

   // String lastEtag,cachedData;
    public static void makeStringRequest(final int requestType,String url, final Map<String, String> params,
                                         Response.Listener successListener,Response.ErrorListener errorListener){
        //if(requestType== Request.Method.GET&&isCache){
            Cache cache = getRequestQueue().getCache();
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

    private static void readFromWebRequest(final int requestType,String url,final Map<String, String> params,Response.Listener successListener,Response.ErrorListener errorListener, final Cache.Entry entry){
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
        getRequestQueue().add(stringRequest);

    }

    public static RequestQueue getRequestQueue(){
        return RequestQueueSingleton.getInstance(TestApp.getInstance()).getRequestQueue();
    }


    /**
     * add request  to the queue, identified by the tag
     * which can be canceled as @cancelPendingRequestsByTag()
     */
    public <T> void addToRequestQueueByTag(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? null : tag);
        getRequestQueue().add(req);
    }

    public static void cancelPendingRequestsByTag(Object tag) {
        getRequestQueue().cancelAll(tag);

    }


    public static void invalidateCache(String url){
        getRequestQueue().getCache().invalidate(url, true);
    }

    public static void clearAllCache(){
        getRequestQueue().getCache().clear();
    }
    public static void clearParticularCache(String url){
        getRequestQueue().getCache().remove(url);
    }

}
