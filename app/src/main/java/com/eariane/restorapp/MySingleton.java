package com.eariane.restorapp;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/*
Quote from Wikipedia about singletons:
"In software engineering, the singleton pattern is a software design pattern
that restricts the instantiation of a class to one object. This is useful
when exactly one object is needed to coordinate actions across the system."

In this case, we use a singleton because we want to create only one way to
access Volley's Request Queue, which decides which request has priority
among the others.

It's safe to copy and paste this class exactly as it is.
*/
public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    private MySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static synchronized MySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MySingleton(context);
        }
        return mInstance;
    }

    public <T> void addJsonArrayToRequestQueue(JsonArrayRequest req) {
        getRequestQueue().add(req);
    }
    public <T> void addJsonObjectToRequestQueue(JsonObjectRequest req) {
        getRequestQueue().add(req);
    }
    public <T> void addStringToRequestQueue(StringRequest req) {
        getRequestQueue().add(req);
    }
}

