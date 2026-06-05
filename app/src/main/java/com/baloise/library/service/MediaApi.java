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

    /**
     * Gibt ein einzelnes Medium anhand der ID zurück.
     *
     * @param id die Medien-ID
     * @return das gefundene Medium
     */
    @GET("/bibliothek/medium/{id}")
    Call<Medium> getMedia(@Path("id") Long id);

    /**
     * Gibt alle Medien zurück.
     *
     * @return Liste aller Medien
     */
    @GET("/bibliothek/medien")
    Call<List<Medium>> getMedias();

    /**
     * Erstellt ein neues Medium.
     *
     * @param medium das zu erstellende Medium
     * @return das erstellte Medium
     */
    @POST("/bibliothek/medium")
    Call<Medium> createMedia(@Body Medium medium);

    /**
     * Aktualisiert ein bestehendes Medium.
     *
     * @param id die ID des zu bearbeitenden Mediums
     * @param medium das aktualisierte Medium-Objekt
     * @return das aktualisierte Medium
     */
    @PATCH("/bibliothek/medium/{id}")
    Call<Medium> editMedia(@Path("id") Long id, @Body Medium medium);

    /**
     * Löscht ein Medium anhand der ID.
     *
     * @param id die ID des zu löschenden Mediums
     */
    @DELETE("/bibliothek/medium/{id}")
    Call<Void> deleteMedia(@Path("id") Long id);

}
