package com.sample.NetworkTaskWithVolley.parser;

import android.content.Context;

import com.google.gson.Gson;
import com.sample.NetworkTaskWithVolley.R;
import com.sample.NetworkTaskWithVolley.model.Books;

import java.io.InputStream;


/**
 * Created by behurm on 4/12/18.
 */

public class JSONParser {

    private final String TAG = JSONParser.class.getName();
    private GsonService gsonService;
    Gson gson;
    private Context mContext;


    public JSONParser(Context context){
        this.mContext = context;
        gson = new Gson();
        gsonService = new GsonService();
    }

    public Books parseBooksResponse(String json){
        if(json == null){
            json = getJsonStringFromRawFile(R.raw.books);//TODO Remove this for production
        }
        if (gsonService != null) {
            Books books = (Books)gsonService.getObject(gson,json,Books.class);
            if(books != null){
                return books;
            }
        }
        return  null;
    }


    private String getJsonStringFromRawFile(int rawId){
        String json = null;

        InputStream is = mContext.getResources().openRawResource(rawId);
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
        }

        return json;
    }
}
