package com.phongbm.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ServiceImpl service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        service = new ServiceImpl();
        // fetchData();
        // computeMath();
        addNumbers();
    }

    private void fetchData() {
        service.getService().getUsers().enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                ArrayList<User> users = response.body();
                Log.d(TAG, users.toString());
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void computeMath() {
        service.getService().sum(5, 20).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    Object object = response.body();
                    String data = new Gson().toJson(object);
                    JSONObject json = new JSONObject(data);

                    int result = json.getInt("result");
                    Log.d(TAG, "Result: " + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void addNumbers() {
        JsonObject req = new JsonObject();
        req.addProperty("number_a", 52);
        req.addProperty("number_b", -20);

        service.getService().add(req).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    Object object = response.body();
                    String data = new Gson().toJson(object);
                    JSONObject json = new JSONObject(data);

                    int result = json.getInt("result");
                    Log.d(TAG, "Result: " + result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
