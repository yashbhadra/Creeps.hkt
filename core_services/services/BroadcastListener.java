package com.creeps.hkthn.core_services.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by rohan on 6/10/17.
 */

public class BroadcastListener extends BroadcastReceiver {
    public static final String RESTART_SERVICE="com.creeps.Hkthn.core_services.services.RestartService";
    private final static String TAG="BroadcastListener";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"inOnReceive");
        if (intent.getAction().equals(RESTART_SERVICE)){
            context.startService(new Intent(context,LocationService.class));
        }
    }
}
