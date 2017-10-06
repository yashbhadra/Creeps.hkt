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
    public static final String DEVICE_RESTART="android.intent.action.BOOT_COMPLETED";
    private final static String TAG="BroadcastListener";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,"inOnReceive");
        if (intent.getAction().equals(RESTART_SERVICE) || intent.getAction().equals(DEVICE_RESTART)){
            context.startService(new Intent(context,LocationService.class));
        }
    }
}
