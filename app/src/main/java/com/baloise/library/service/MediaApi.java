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

/**
 * Retrofit-API-Interface für Medien-Endpunkte.
 *
 * @author Julian Schiller
 */
public interface MediaApi {

    @GET("/bibliothek/medium/{id}")
    Call<Medium> getMedia(@Path("id") Long id);


    @GET("/bibliothek/medien")
    Call<List<Medium>> getMedias();

    @POST("/bibliothek/medium")
    Call<Medium> createMedia(@Body Medium medium);

    @PATCH("/bibliothek/medium/{id}")
    Call<Medium> editMedia(@Path("id") Long id, @Body Medium medium);

    @DELETE("/bibliothek/medium/{id}")
    Call<Void> deleteMedia(@Path("id") Long id);

}
