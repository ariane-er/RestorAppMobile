package com.eariane.restorapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

// A class to manage all the request necessary for the App
public class Requests {
    public Context mContext;
    private static final String TAG = "Requests";
    public Requests(Context context) {
        mContext = context;
    }

    // Interfaces provide a way to keep coding with data from this class in another context
    public interface VolleyCallback {
        void onSuccess(String resp);
        void onFailure(String error);
    }

    public void getRestaurants(final VolleyCallback callback) {
        // Here goes the url of your endpoint
        String url = "http://erariane.pythonanywhere.com/restaurants";
        // Remember to specify the Method you will use
        int  requestMethod = Request.Method.GET;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(requestMethod, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                callback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
                Log.d(TAG, "onErrorResponse: " + error.toString());
            }
        }){
            @Override
            /*
            Here you declare the headers of the request. Headers are data that explain about the
            contents of the request.
            */
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent","android");
                headers.put("Content-Type","application/json");
                return headers;
            }
        };

        // After everything is ready, add the request to the Volley's Request Queue
        MySingleton.getInstance(mContext).addJsonArrayToRequestQueue(jsonArrayRequest);
    }

    public void login(final String username, final String password, final VolleyCallback callback) {
        String url = "http://erariane.pythonanywhere.com/processLogIn";
        final int  requestMethod = Request.Method.POST;
        StringRequest stringRequest = new StringRequest(requestMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse - resp: " + response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                callback.onFailure(error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent","android");
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("logInUsername",username);
                params.put("logInPassword",password);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addStringToRequestQueue(stringRequest);
    }

    public void signup(final String username, final String password, final String confirm,  final VolleyCallback callback) {
        String url = "http://erariane.pythonanywhere.com/processSignUp";
        final int  requestMethod = Request.Method.POST;
        StringRequest stringRequest = new StringRequest(requestMethod, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "onResponse - resp: " + response);
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.toString());
                callback.onFailure(error.toString());
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("User-Agent","android");
                headers.put("Content-Type","application/x-www-form-urlencoded");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("signUpUsername",username);
                params.put("signUpPassword",password);
                params.put("signUpConPassword",confirm);
                return params;
            }
        };
        MySingleton.getInstance(mContext).addStringToRequestQueue(stringRequest);
    }

}


