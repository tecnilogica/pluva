package com.tecnilogica.pluva.net;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Lucia on 11/5/16.
 */
public interface RetrofitInterface {

    @FormUrlEncoded
    @POST("/forecast.php")
    void sendValuesForToday(@Field(value = "place") String location, @Field("u") String userId, Callback<String> callback);

    @FormUrlEncoded
    @POST("/forecast.php")
    void sendValuesForTomorrow(@Field(value = "place") String location, @Field("u") String userId, @Field("tomorrow") boolean tomorrow, Callback<String> callback);
}
