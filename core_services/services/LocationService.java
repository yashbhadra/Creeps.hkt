package com.creeps.hkthn.core_services.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;

import com.creeps.hkthn.core_services.services.loc.LocationHandler;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by rohan on 6/10/17.
 */

public class LocationService extends Service {

    private boolean obtainLocation;
    private LocationHandler mLocationHandler;


    public class LocalBinder extends Binder{
        public LocationService getService(){
            return LocationService.this;
        }
    }

    private final IBinder mBinder=new LocalBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        this.obtainLocation=true;
        LocationHandler.prepareInstance("mLocationHanlder",getApplicationContext());
        Log.d(TAG,"inOnCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG,"connected to the service");
        LocationHandler.getInstance().start();
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent){
        return this.mBinder;
    }


    @Override
    public void onDestroy() {
        //super.onDestroy();
        this.sendBroadcast(new Intent(BroadcastListener.RESTART_SERVICE));
        LocationHandler.getInstance().quit();
        Log.d(TAG,"inOnDestroy");
    }




    public void setObtainLocation(boolean b){
        this.mLocationHandler.setShouldObtainLocation(b);
    }
}
