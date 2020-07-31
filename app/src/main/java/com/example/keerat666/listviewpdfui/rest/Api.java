package com.example.keerat666.listviewpdfui.rest;

import com.example.keerat666.listviewpdfui.models.LoginData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface Api {

    @POST(ApiURLs.LOGIN_API)
    Call<LoginData> getLoginData(@Body LoginData loginData);
}
