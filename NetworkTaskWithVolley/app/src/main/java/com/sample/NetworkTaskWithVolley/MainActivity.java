package com.sample.NetworkTaskWithVolley;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.JsonObject;
import com.sample.NetworkTaskWithVolley.model.Books;
import com.sample.NetworkTaskWithVolley.networkhandler.NetworkConstants;
import com.sample.NetworkTaskWithVolley.networkhandler.RequestQueManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements RequestQueManager.RequestQueListener {

    final String TAG = getClass().getName();

    RequestQueManager mRequestQueManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRequestQueManager = RequestQueManager.getInstance(this);


        //Get the response as Jsonobject
        mRequestQueManager.getJsonRequest(NetworkConstants.ANIMALSAPITOKEN, NetworkConstants.URL.animalAPIUrl,this);

        //Get the response as GsonObject
        mRequestQueManager.getGSONRequest(NetworkConstants.BOOKSAPITOKEN, NetworkConstants.URL.booksAPIUrl,this);

    }

    @Override
    public void onSucces(Object response, String token) {
        switch (token){
            case NetworkConstants.ANIMALSAPITOKEN:
                //It's the response for json object request
                JSONObject jsonResponse = (JSONObject) response;
                Log.i(TAG,"response-----------"+response.toString());
                try {
                    if (jsonResponse.has("count"))
                        Log.i(TAG, "animals.count-----------" + jsonResponse.getString("count"));
                   JSONArray entries =  jsonResponse.getJSONArray("entries");
                    for(int i=0; i < entries.length(); i++){
                        JSONObject jsonObject = entries.getJSONObject(i);
                        Log.i(TAG,"Animal.API-----------"+jsonObject.getString("API"));
                        Log.i(TAG,"Animal.Description-----------"+jsonObject.getString("Description"));
                        Log.i(TAG,"Animal.Link-----------"+jsonObject.getString("Link"));
                    }
                }catch (JSONException jsone){
                    jsone.printStackTrace();
                }
                break;
            case NetworkConstants.BOOKSAPITOKEN:
                //It's the response for Gson object request
                Books books = (Books)response;
                Log.i(TAG,"books.count-----------"+books.count);
                for (Books.Book book: books.entries) {
                    Log.i(TAG,"Book.API-----------"+book.API);
                    Log.i(TAG,"Book.Description-----------"+book.description);
                    Log.i(TAG,"Book.Link-----------"+book.link);
                }
                break;
        }
    }

    @Override
    public void onError(int statusCode, String error, String token) {
        switch (token){
            case NetworkConstants.ANIMALSAPITOKEN:
                //It's the response for json object request
                Log.i(TAG,"statusCode-----------"+error+"---------"+token);
                break;
            case NetworkConstants.BOOKSAPITOKEN:
                Log.i(TAG,"statusCode-----------"+error+"---------"+token);
                break;
        }
    }
}
