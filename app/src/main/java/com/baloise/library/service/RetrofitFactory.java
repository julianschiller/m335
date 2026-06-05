package com.baloise.library.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Singleton-Factory zur Erzeugung der Retrofit-Instanz.
 *
 * @author Julian Schiller
 */
public class RetrofitFactory {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://192.168.1.193:8080";

    /**
     * Gibt die Singleton-Retrofit-Instanz zurück und erstellt sie bei Bedarf.
     *
     * @return die konfigurierte Retrofit-Instanz
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
