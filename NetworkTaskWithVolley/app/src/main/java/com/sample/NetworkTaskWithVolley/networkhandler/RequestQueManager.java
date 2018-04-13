package com.sample.NetworkTaskWithVolley.networkhandler;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sample.NetworkTaskWithVolley.model.Books;
import com.sample.NetworkTaskWithVolley.parser.JSONParser;
import com.sample.NetworkTaskWithVolley.utils.Utility;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by behurm on 4/11/18.
 */

public class RequestQueManager {

    /**
     * Interface defines HTTP request headers key and values
     */
    public interface HttpRequestHeaderKeyValue {

        // Keys
        String CONTENT_TYPE = "content-type";
        String ACCEPT = "accept";
        String AUTHORISATION = "Authorization";
        String TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
        String TYPE_JSON = "application/json";
        String TYPE_PDF = "application/pdf";
        String TYPE_JSON_XML = "json/xml";
        String TYPE_OCTET = "application/octet-stream";
        String ACCEPT_ALL = "*/*";
        // Values
        String CHAR_SET = "UTF8";
        String BASIC = "Basic ";
        String GET = "GET";

    }

    public String TAG = getClass().getName();
    private static RequestQueManager mInstance;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private RequestQueManager(Context context){
        this.mContext = context;
        mRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public static RequestQueManager getInstance(Context context){
        if(mInstance == null)
            mInstance = new RequestQueManager(context);

        return mInstance;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /*
    * below function is to get the response as JsonObject
    */
    public void getJsonRequest(final String apiToken, String url,final RequestQueListener listener){

        Map<String,String> headers = new HashMap<>();
        headers.put(HttpRequestHeaderKeyValue.ACCEPT, HttpRequestHeaderKeyValue.ACCEPT_ALL);
        headers.put(HttpRequestHeaderKeyValue.CONTENT_TYPE, HttpRequestHeaderKeyValue.TYPE_JSON);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    listener.onSucces(response,apiToken);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorJson = null;
                try {
                    if (error.networkResponse!=null && error.networkResponse.data != null) {
                        Log.i(TAG,"Inside onErrorResponse fetchSocialProfiles"+error.networkResponse.statusCode);
                        errorJson = new String(
                                error.networkResponse.data,
                                HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        listener.onError(error.networkResponse.statusCode,errorJson,apiToken);
                    }
                }catch (UnsupportedEncodingException e){
                    listener.onError(error.networkResponse.statusCode,errorJson,apiToken);
                }
            }
        });

        if(Utility.isInternetConnected(mContext)){
            addToRequestQueue(jsonObjectRequest);
        }else{
            if(mRequestQueue.getCache().get(url)!=null){
                //response exists
                String cachedResponse = new String(mRequestQueue.getCache().get(url).data);
                Log.i(TAG,"Inside onresponse fetchSocialProfiles"+cachedResponse);

            }
        }
    }

    public void getGSONRequest(final String apiToken, String url,final RequestQueListener listener){
        Map<String,String> headers = new HashMap<>();
        headers.put(HttpRequestHeaderKeyValue.ACCEPT, HttpRequestHeaderKeyValue.ACCEPT_ALL);
        headers.put(HttpRequestHeaderKeyValue.CONTENT_TYPE, HttpRequestHeaderKeyValue.TYPE_JSON);

        GsonRequest<Books> gsonRequest = new GsonRequest<Books>(Request.Method.GET,url,Books.class,headers,
                new Response.Listener<Books>() {
                    @Override
                    public void onResponse(Books response) {
                        listener.onSucces(response,apiToken);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String errorJson = null;
                        try {
                            if (error.networkResponse!=null && error.networkResponse.data != null) {
                                Log.i(TAG,"Inside onErrorResponse fetchSocialProfiles"+error.networkResponse.statusCode);
                                errorJson = new String(
                                        error.networkResponse.data,
                                        HttpHeaderParser.parseCharset(error.networkResponse.headers));
                                listener.onError(error.networkResponse.statusCode,errorJson,apiToken);
                            }
                        }catch (UnsupportedEncodingException e){
                            listener.onError(error.networkResponse.statusCode,errorJson,apiToken);
                        }
                    }
                });

        gsonRequest.setTag(NetworkConstants.BOOKSAPITOKEN);
        if(Utility.isInternetConnected(mContext)){
            addToRequestQueue(gsonRequest);
        }else{
            if(mRequestQueue.getCache().get(url)!=null){
                //response exists
                String cachedResponse = new String(mRequestQueue.getCache().get(url).data);

                Log.i(TAG,"Inside onresponse fetchSocialProfiles"+cachedResponse);
                Books books = (Books)new JSONParser(mContext).parseBooksResponse(cachedResponse);
                listener.onSucces(books,apiToken);
            }
        }
    }

    public interface  RequestQueListener{
        void onSucces(Object response,String token);
        void onError(int statusCode,String error,String token);
    }
}

