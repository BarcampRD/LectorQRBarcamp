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
    Call<String> confirmar(@Query(value = "id", encoded = true) String query);
    @GET("consultarRegistro/")
    Call<Registro> consultar(@Query(value = "id", encoded = true) String query);
}