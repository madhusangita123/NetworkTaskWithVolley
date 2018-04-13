package com.sample.NetworkTaskWithVolley.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by behurm on 4/12/18.
 */

public class Utility {

    public static boolean isInternetConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo networkInfo : netInfo) {

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
                if (networkInfo.isConnected())
                    return true;

            if (networkInfo.getType() == ConnectivityManager.TYPE_WIMAX) // WIMAX//
                // means//
                // 4G
                if (networkInfo.isConnected())
                    return true;

            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
                if (networkInfo.isConnected())
                    return true;

        }

        return false;

    }
}
