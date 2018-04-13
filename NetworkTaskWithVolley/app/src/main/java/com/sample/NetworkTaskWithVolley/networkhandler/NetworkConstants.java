package com.sample.NetworkTaskWithVolley.networkhandler;

/**
 * Created by behurm on 4/12/18.
 */

public class NetworkConstants {

    public final static String ANIMALSAPITOKEN = "animals_api";
    public final static String BOOKSAPITOKEN = "books_api";

    public  interface  URL{
        final String animalAPIUrl = "https://api.publicapis.org/entries?category=animals&https=true";
        final String booksAPIUrl = "https://api.publicapis.org/entries?category=Books&https=true";
    }
}
