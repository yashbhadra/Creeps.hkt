package com.creeps.hkthn.core_services.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by rohan on 6/10/17.
 */

public class SharedPreferenceManager {
    private Context mContext;
    private static SharedPreferenceManager mSharedPreferenceHandler;/* to be used for singleton*/
    private final SharedPreferences sharedPreferences;/* to be used for persisting essential data across sessions*/
    private final String SHARED_PREF_NAME="Sl_pref";/* name of the sharePref file*/
    private final static String TAG="SharePreferenceManager";
    private SharedPreferenceManager(Context context){
        this.mContext=context;
        this.sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
    }


    /* returns an instance of the this class*/
    public static SharedPreferenceManager getInstance(Context context){
        if(SharedPreferenceManager.mSharedPreferenceHandler == null){
            SharedPreferenceManager.mSharedPreferenceHandler = new SharedPreferenceManager(context);
        }

        return SharedPreferenceManager.mSharedPreferenceHandler;
    }

    /* returns a string with the specified key. if key is absent returns null*/
    public String getString(String x){
        return this.sharedPreferences.getString(x,null);
    }
    public void add(String key,String value){
        SharedPreferences.Editor editor=this.sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();/* commit asynchronously*/
    }
    public void addBoolean(String key,boolean val){
        SharedPreferences.Editor editor=this.sharedPreferences.edit();
        editor.putBoolean(key,val);
        editor.apply();
    }
    /* returns a boolean . if the specified key is absent then it returns false*/
    public boolean getBoolean(String key){
        return this.sharedPreferences.getBoolean(key,false);
    }


}
