package com.baloise.library.service;

import com.baloise.library.common.Ausleihe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Retrofit-API-Interface für Ausleihe-Endpunkte.
 *
 * @author Julian Schiller
 */
public interface BorrowingApi {
    /**
     * Gibt alle Ausleihen zurück.
     *
     * @return Liste aller Ausleihen
     */
    @Headers("Accept: application/json")
    @GET("/bibliothek/ausleihen")
    Call<List<Ausleihe>> getBorrowings();

    /**
     * Gibt eine einzelne Ausleihe anhand der Medien-ID zurück.
     *
     * @param id die Medien-ID
     * @return die gefundene Ausleihe
     */
    @Headers("Accept: application/json")
    @GET("/bibliothek/ausleihe/medium/{id}")
    Call<Ausleihe> getBorrowing(@Path("id") Long id);

    /**
     * Verlängert eine bestehende Ausleihe.
     *
     * @param id die Medien-ID der Ausleihe
     * @return die aktualisierte Ausleihe
     */
    @Headers("Content-Type: application/json")
    @PATCH("/bibliothek/ausleihe/medium/{id}")
    Call<Ausleihe> extendBorrowing(@Path("id") Long id);

    /**
     * Erstellt eine neue Ausleihe.
     *
     * @param borrowing die zu erstellende Ausleihe
     * @return die erstellte Ausleihe
     */
    @Headers("Content-Type: application/json")
    @POST("/bibliothek/ausleihe")
    Call<Ausleihe> createBorrowing(@Body Ausleihe borrowing);

    /**
     * Löscht eine Ausleihe anhand der Medien-ID.
     *
     * @param id die Medien-ID der zu löschenden Ausleihe
     */
    @DELETE("/bibliothek/ausleihe/medium/{id}")
    Call<Void> deleteBorrowing(@Path("id") Long id);
}
