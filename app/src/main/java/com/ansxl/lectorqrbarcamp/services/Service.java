package com.ansxl.lectorqrbarcamp.services;

import com.ansxl.lectorqrbarcamp.entities.Registro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("registros")
    Call<List<Registro>> getRegistros();
    @POST("confirmar/")
    Call<Registro> getQuestions(@Query(value = "id", encoded = true) String query);
}