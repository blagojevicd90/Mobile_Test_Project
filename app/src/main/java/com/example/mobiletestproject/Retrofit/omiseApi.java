package com.example.mobiletestproject.Retrofit;

import com.example.mobiletestproject.Model.CharityModel;
import com.example.mobiletestproject.Model.DonationModel;
import com.google.gson.JsonObject;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface omiseApi {

    @GET("charities")
    Observable<List<CharityModel>> getCharities();

    @POST("donations")
    Observable<DonationModel> getDonations(@Body JsonObject jsonObject);
}
