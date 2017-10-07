package com.creeps.hkthn.core_services.networking.nw_thread.retrofit;

import android.content.Context;
import android.util.Log;

import com.creeps.hkthn.R;
import com.creeps.hkthn.core_services.utils.FirebaseLoginManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rohan on 30/9/17.
 * this class acts as a wrapper for the retrofit client. It has a factory method which requires Context( to prevent gcing) which returns an instance of the class.
 * The context is also used to obtain the BASE_URL from the string.xml file.
 * It has the following functions
 *  1. authorizeUser() sends an object in the Reverberator provided. if the response fails it passes in null;
 *  2. getBaseURL() returns the BASE_URL which is specified in Resources/String file with id base_url
 *  3. getUserDetailsWithID() sends an object in the Reverberator provided. if the response fails it passes in null
 */

public class RetrofitApiClient {
    private static RetrofitApiClient mRetrofitApiClient;
    private Context mContext;
    private final String BASE_URL;
    private final Retrofit mRetrofitInstance;
    private final RetrofitApiInterface mRetrofitApiInterface;
    private final static String TAG="RetrofitApiClient";


    private final static String API_KEY=null;


    /* Context is being takes as a param to prevent the object from getting gc'd. As long as a reference to the other object is alive and is in the young pool the other object can
    * not be gc'd hence the current wont be gcd*/
    private RetrofitApiClient(Context context){
        this.mContext=context;
        this.BASE_URL=this.mContext.getResources().getString(R.string.base_url);
        this.mRetrofitInstance= new Retrofit.Builder().baseUrl(this.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        this.mRetrofitApiInterface= this.mRetrofitInstance.create(RetrofitApiInterface.class);

    }
    /* use this factory method to obtain an instance of the class*/
    public static RetrofitApiClient getInstance(Context context){
        if(RetrofitApiClient.mRetrofitApiClient==null){
            RetrofitApiClient.mRetrofitApiClient = new RetrofitApiClient(context);
        }
        return RetrofitApiClient.mRetrofitApiClient;
    }
    /*this function is a generalized one to be used for all call objects ... will pass in the value returned by response ...
    it is the responsibility of the class which implements Reverberator to cast it to his/her liking
    * */

    private void enque(Call call,final Reverberator reverberator){
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if(reverberator!=null) {
                    reverberator.reverb(response.body());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d(TAG,"Couldnt fetch data from server ... read the stackTrace for more info");
                t.printStackTrace();
                if(reverberator!=null)
                    reverberator.reverb(null);
            }
        });
    }

    public String getBaseUrl() {
        return this.BASE_URL;
    }

    public void getGroups(float longi,float latli,Reverberator reverberator){
        Call<List<String>> call=this.mRetrofitApiInterface.getSubscribableGroups(longi,latli, FirebaseLoginManager.getInstance().getEmail());
    }







}
