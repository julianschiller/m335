package com.baloise.library.service;

import com.baloise.library.common.Medium;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MediaApi {

    @GET("/bibliothek/medien")
    Call<List<Medium>> getMedia();

    @POST("medium")
    Call<Medium> createMedia(@Body Medium medium);

    @PATCH("medium/{id}")
    Call<Medium> editMedia(@Path("id") int id, Medium medium);

    @DELETE("medium/{id}")
    Call<Void> deleteMedia(@Path("id") int id);

}
