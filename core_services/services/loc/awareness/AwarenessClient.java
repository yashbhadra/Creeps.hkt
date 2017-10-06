package com.creeps.hkthn.core_services.services.loc.awareness;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

/**
 * Created by rohan on 6/10/17.
 */

public class AwarenessClient implements AwarenessConstants{
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private static AwarenessClient mAwarenessClient=null;


    private AwarenessClient(Context context){
        this.mContext=context;
        this.mGoogleApiClient=new GoogleApiClient.Builder(context).addApi(Awareness.API).build();
        this.mGoogleApiClient.connect();
    }

    public static AwarenessClient prepareInstance(Context context){
        return AwarenessClient.mAwarenessClient= AwarenessClient.mAwarenessClient!=null?AwarenessClient.mAwarenessClient:new AwarenessClient(context);
    }
    public static AwarenessClient getInstance(){
        return AwarenessClient.mAwarenessClient;
    }
    public static boolean checkLocationPerms(Context context){
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void detectLocation(  final LocationCallback locationCallback){
        /* TODO add check for perms */
        if(!AwarenessClient.checkLocationPerms(this.mContext))
            return;

        Log.d(TAG,"HERE");
        try {
            Awareness.SnapshotApi.getLocation(this.mGoogleApiClient).setResultCallback(new ResultCallback<LocationResult>() {
                @Override
                public void onResult( @NonNull LocationResult locationResult) {

                    Location location=locationResult.getLocation();
                    Log.d(TAG,"lat "+location.getLatitude()+" longi "+location.getLongitude());
                    if(locationCallback!=null)
                        locationCallback.currentLocation(location);



                }
            });
        }catch (SecurityException se){
            Log.wtf(TAG," should have never happened ");
            se.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }


    }





}
interface AwarenessConstants{
    public static final String TAG="AwarenessClient";
}