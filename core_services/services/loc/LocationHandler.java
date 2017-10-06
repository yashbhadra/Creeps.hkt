package com.creeps.hkthn.core_services.services.loc;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;

import com.creeps.hkthn.core_services.services.loc.awareness.AwarenessClient;
import com.creeps.hkthn.core_services.services.loc.awareness.LocationCallback;

import java.util.concurrent.TimeoutException;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by rohan on 6/10/17.
 */

public class LocationHandler extends HandlerThread implements LocationCallback,Handler.Callback,LocationHandlerConstants{

    private Handler mHandler;
    private Runnable mRunnable;
    private Context mContext;
    private boolean shouldObtainLocation;

    private static LocationHandler mLocationHandler=null;
    private LocationHandler(String name,Context context){
        super(name);
        this.mContext=context;
        AwarenessClient.prepareInstance(context);
        this.mRunnable=new Runnable() {
            @Override
            public void run() {
                System.out.println("in run");
                AwarenessClient.getInstance().detectLocation(LocationHandler.this);
                if(LocationHandler.this.mHandler!=null && LocationHandler.this.shouldObtainLocation)
                    LocationHandler.this.mHandler.postDelayed (this,LocationHandler.TIMEOUT_DELAY);
            }
        };
        this.shouldObtainLocation=true;

        Log.d(TAG,"INSTANTIATED locationHandler");

    }
    public void setShouldObtainLocation(boolean b){
        this.shouldObtainLocation=b;
    }


    public static LocationHandler prepareInstance(String name,Context context){
        return LocationHandler.mLocationHandler= LocationHandler.mLocationHandler!=null? LocationHandler.mLocationHandler:  new LocationHandler(name,context);
    }
    
    public static LocationHandler getInstance(){
        return LocationHandler.mLocationHandler;
    }

    @Override
    protected void onLooperPrepared() {
        Log.d(TAG,"inonPrepared");
        this.mHandler=new Handler(this.getLooper(),this);
        this.mHandler.postDelayed(this.mRunnable, TIMEOUT_DELAY);
    }

    @Override
    public boolean handleMessage(Message msg) {

        System.out.println("in handler");
        System.out.println(Thread.currentThread().getName());
        return true;
    }

    /* LocationCallback methods for LocationHandler*/
    @Override
    public void currentLocation(Location location) {
        Log.d(TAG,location.toString());
    }
}
interface LocationHandlerConstants{
    public static final String TAG="LocationHandler";

    public static final int TIMEOUT_DELAY=1000;
}
