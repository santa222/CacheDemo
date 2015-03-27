/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.test.mytest.network.request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import org.apache.http.Header;
import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * A canned request for retrieving the response body at a given URL as a String.
 */
public class VolleyRequest extends Request<String> {
    private Listener<String> mListener;
    private Cache.Entry entry;

    /**
     * Creates a new request with the given method.
     *
     * @param method the request {@link com.android.volley.Request.Method} to use
     * @param url URL to fetch the string at
     * @param listener Listener to receive the String response
     * @param errorListener Error listener, or null to ignore errors
     */
    public VolleyRequest(int method, String url, Listener<String> listener,
                         ErrorListener errorListener,Cache.Entry entry) {
        super(method, url, errorListener);
        mListener = listener;
        this.entry=entry;
        if(entry!=null){
            Log.v("222","last etag: "+entry.etag);
        }
    }



    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed=null;
        //Log.v("222","response etag: "+response.headers.get("ETag"));
        Log.v("222","statuscode: "+response.statusCode);
        if(response.statusCode== HttpStatus.SC_NOT_MODIFIED){
            try {
                parsed=new String(entry.data, "UTF-8");
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            try {
                parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } catch (UnsupportedEncodingException e) {
                parsed = new String(response.data);
            }
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

/*

    @Override
    protected Response<T> parseNetworkResponse( NetworkResponse response) {
        try {
            String json = new String(response.data,HttpHeaderParser.parseCharset(response.headers));

            return Response.success(gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        }
        // handle errors
        ...
    }
*/


    /**
     * Passing some request headers
     * */
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        //headers.put("Content-Type", "application/json");
        //headers.put("apiKey", "xxxxxxxxxxxxxxx");
       // headers.put("Accept", "application/json");
        if(entry!=null&&entry.etag!=null)
            headers.put("If-None-Match", entry.etag);

        Log.v("222","----send head----- ");
        for(String key:headers.keySet()){
            Log.v("222","key: "+key+" , "+headers.get(key));
        }
        Log.v("222","----------------- ");
        return headers;
    }
}
