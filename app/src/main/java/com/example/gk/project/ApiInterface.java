package com.example.gk.project;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Gurakuq Krasniqi on 4/8/2018.
 */

public interface ApiInterface {
    @GET("events")
    Call<EventResponse> getEvents(@Query("lat") double lat, @Query("lng") double lon,@Query("distance") int distance,@Query("accessToken") String accessToken);


}
