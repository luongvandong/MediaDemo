package com.phongbm.retrofitdemo;

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @GET(value = "/users/list")
    Call<ArrayList<User>> getUsers();

    // .../sum?a=5&&b=10
    @GET(value = "/sum")
    Call<Object> sum(@Query("a") int a, @Query("b") int b);

    // .../users/1
    @GET(value = "/user/:id")
    Call<User> getUser(@Path("id") String id);

    @POST(value = "/add")
    Call<Object> add(@Body JsonObject request);

}