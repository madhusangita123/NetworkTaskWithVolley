package com.sample.NetworkTaskWithVolley.parser;

import com.google.gson.Gson;

/**
 * Created by behurm on 4/12/18.
 */
public class GsonService {

    public Object getObject(Gson gson, String json, Class object){
        return gson.fromJson(json, object);
    }
}
