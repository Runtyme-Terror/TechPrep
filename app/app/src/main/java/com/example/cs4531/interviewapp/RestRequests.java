package com.example.cs4531.interviewapp;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class RestRequests {
    private static RestRequests instance;
    private RequestQueue rqueue;// Instantiate the cache
    private Cache cache;
    private Context ctx;

    private RestRequests(Context context){
        ctx = context;
        if(rqueue == null)
            rqueue = Volley.newRequestQueue(ctx.getApplicationContext());

    }

    public static RestRequests getInstance(Context context){
        if(instance == null){
            instance = new RestRequests(context);
        }
        return instance;
    }

    public <T> void addToRequestQueue(Request<T> r){
        rqueue.add(r);
    }

}