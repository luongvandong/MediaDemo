package com.phongbm.retrofitdemo;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("full_name")
    private String fullName;

    @SerializedName("age")
    private int age;

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullName + '\'' +
                ", age=" + age +
                '}';
    }

}