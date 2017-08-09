package com.phongbm.retrofitdemo;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceImpl {
    private Service service;

    public ServiceImpl() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.137.1:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
    }

    public Service getService() {
        return service;
    }

}