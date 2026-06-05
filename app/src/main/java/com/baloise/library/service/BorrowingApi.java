package com.baloise.library.service;

import com.baloise.library.common.Ausleihe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BorrowingApi {
    @GET("/bibliothek/ausleihen")
    Call<List<Ausleihe>> getBorrowings();

    @GET("/bibliothek/ausleihe/medium/{id}")
    Call<Ausleihe> getBorrowing(@Path("id") Long id);

    @PATCH("/bibliothek/ausleihe/medium/{id}")
    Call<Ausleihe> extendBorrowing(@Path("id") Long id);

    @POST("/bibliothek/ausleihe")
    Call<Ausleihe> createBorrowing(@Body Ausleihe borrowing);
}
