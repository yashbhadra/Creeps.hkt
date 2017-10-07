package com.creeps.hkthn.core_services.networking.nw_thread.retrofit;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by rohan on 30/9/17.
 *
 */

public interface RetrofitApiInterface {


    @GET("/quizapp/studentdetail.php")
    public Call<List<String>> getSubscribableGroups(@Query("latitude") float lat, @Query("longitude") float longitude,@Query("username")String username );

}
