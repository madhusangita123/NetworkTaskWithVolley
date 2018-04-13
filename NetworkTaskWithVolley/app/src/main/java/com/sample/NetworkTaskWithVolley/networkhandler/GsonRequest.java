package com.sample.NetworkTaskWithVolley.networkhandler;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpStatus;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by behurm on 4/11/18.
 */

public class GsonRequest<T> extends Request<T> {

    public String TAG = getClass().getName();

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private String parameters = null;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        Log.i(TAG,"Inside GsonRequest");
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
    }

    public GsonRequest(int method, String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, String parameters) {
        this(method, url, clazz, headers, listener, errorListener);
        this.parameters = parameters;
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Log.i(TAG,"Inside parseNetworkResponse");
        try {
            if(response.statusCode == HttpStatus.SC_OK || response.statusCode == HttpStatus.SC_CREATED){

                String json = new String(
                        response.data,
                        HttpHeaderParser.parseCharset(response.headers));
                Log.i(TAG,"*************************VOLLEY TEST************************"+response.statusCode+"***"+ HttpHeaderParser.parseCacheHeaders(response));
                Log.i(TAG,json);
                return Response.success(
                        gson.fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));

            }else{
                if(response!=null && response.data != null){
                  String errorJson = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                     VolleyError volleyError = new VolleyError(errorJson);
                    return  Response.error(volleyError);
                }
                return  Response.error(new VolleyError(response));
            }


        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            volleyError = new VolleyError(volleyError.networkResponse);
        }

        return volleyError;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        // headers.put(RequestQueManager.HttpRequestHeaderKeyValue.ACCEPT, HttpManager.HttpRequestHeaderKeyValue.TYPE_JSON);
        return headers;
    }
}
