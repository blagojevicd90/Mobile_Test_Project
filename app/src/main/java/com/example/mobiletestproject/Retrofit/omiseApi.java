package com.example.mobiletestproject.Retrofit;

import com.example.mobiletestproject.Model.CharityModel;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface omiseApi {

    @GET("charities")
    Observable<List<CharityModel>> getCharities();
}
