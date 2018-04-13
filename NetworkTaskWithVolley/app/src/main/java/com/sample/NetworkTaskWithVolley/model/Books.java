package com.sample.NetworkTaskWithVolley.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by behurm on 4/12/18.
 */

public class Books {

    @SerializedName("count")
    public int count;

    @SerializedName("entries")
    public List<Book> entries;

    public class Book{
        @SerializedName("API")
        public String API;
        @SerializedName("Description")
        public String description;
        @SerializedName("Link")
        public String link;
    }
}
