package com.baloise.library.service;

import com.baloise.library.common.Ausleihe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BorrowingApi {
    @GET("/bibliothek/ausleihen")
    Call<List<Ausleihe>> getBorrowings();
}
