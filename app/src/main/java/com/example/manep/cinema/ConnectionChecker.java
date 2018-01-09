package com.example.manep.cinema;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by manep on 8/8/2017.
 */

public class ConnectionChecker {
    Context context;
    public ConnectionChecker(Context context){
        this.context=context;
    }
    public boolean isConnected()
    {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        if(connectivity !=null)
        {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if(info != null)
            {
                if(info.getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }
}

