package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.baloise.library.common.Medium;
import com.baloise.library.service.MediaApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity zum Erstellen und Bearbeiten von Medien.
 *
 * @author Julian Schiller
 */
public class MediaManageActivity extends AppCompatActivity {

    private TextInputEditText title;
    private TextInputEditText author;
    private TextInputEditText genre;
    private TextInputEditText isbn;
    private TextInputEditText location_code;
    private TextInputEditText min_age;
    private Button save;
    private Button cancel;

    private TextInputLayout titleLayout;
    private TextInputLayout authorLayout;

    private boolean isCreate;
    private Long id;

    MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

    /** {@inheritDoc} Initialisiert die Eingabefelder und entscheidet zwischen Erstellen und Bearbeiten. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_media_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isCreate = getIntent().getBooleanExtra("create", true);
        id = getIntent().getLongExtra("medium_id", -1);

        title = findViewById(R.id.input_title);
        author = findViewById(R.id.input_author);
        genre = findViewById(R.id.input_genre);
        isbn = findViewById(R.id.input_isbn);
        location_code = findViewById(R.id.input_location_code);
        min_age = findViewById(R.id.input_age);
        save = findViewById(R.id.save_media);
        cancel = findViewById(R.id.cancel_media);
        titleLayout  = findViewById(R.id.layout_title);
        authorLayout = findViewById(R.id.layout_author);
        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);

            view.getContext().startActivity(intent);
        });

        if (isCreate) {
            createMedia();
        } else {
            loadMedia(id);
            editMedia();
        }
    }

    /**
     * Lädt ein bestehendes Medium vom Backend und befüllt die Eingabefelder.
     *
     * @param id die ID des zu ladenden Mediums
     */
    private void loadMedia(Long id) {
        api.getMedia(id).enqueue(new Callback<Medium>() {
            @Override
            public void onResponse(Call<Medium> call, Response<Medium> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Medium media = response.body();
                    title.setText(media.getTitel());
                    author.setText(media.getAutor());
                    genre.setText(media.getGenre() != null ? media.getGenre() : "");
                    isbn.setText(media.getEan() != null ? String.valueOf(media.getEan()) : "");
                    location_code.setText(media.getStandort() != null ? media.getStandort() : "");
                    min_age.setText(media.getFsk() != null ? String.valueOf(media.getFsk()) : "");
                } else {
                    showError("Laden des Mediums nicht möglich");
                }
            }

            @Override
            public void onFailure(Call<Medium> call, Throwable throwable) {
                showError("No Connection");
            }
        });
    }

    /**
     * Konfiguriert den Speichern-Button zum Erstellen eines neuen Mediums.
     */
    private void createMedia() {

        save.setOnClickListener(view -> {
            if (!isValid()){
                return;
            }

            Medium medium = getMedium();
            saveMedia(null, medium);
        });


    }

    /**
     * Validiert die Pflichtfelder Titel und Autor.
     *
     * @return true wenn alle Pflichtfelder ausgefüllt sind
     */
    private boolean isValid() {
        boolean valid = true;

        if (title.getText().toString().trim().isEmpty()) {
            titleLayout.setError("Titel ist erforderlich");
            valid = false;
        } else {
            titleLayout.setError(null);
        }

        if (author.getText().toString().trim().isEmpty()) {
            authorLayout.setError("Autor ist erforderlich");
            valid = false;
        } else {
            authorLayout.setError(null);
        }

        return valid;
    }

    /**
     * Konfiguriert die Ansicht und den Speichern-Button zum Bearbeiten eines Mediums.
     */
    private void editMedia() {
        title.setEnabled(false);
        author.setEnabled(false);

        save.setOnClickListener(view -> {

            Medium medium = getMedium();
            saveMedia(id, medium);
        });
    }

    /**
     * Speichert ein Medium über die API, erstellt oder aktualisiert je nach übergebener ID.
     *
     * @param id die ID des Mediums (null für Neuerstellung)
     * @param medium das zu speichernde Medium-Objekt
     */
    private void saveMedia(Long id, Medium medium) {
        if (id != null) {
            api.editMedia(id, medium).enqueue(new Callback<Medium>() {
                @Override
                public void onResponse(Call<Medium> call, Response<Medium> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(MediaManageActivity.this, MainActivity.class));
                    } else {
                        showError("Bearbeiten des Mediums nicht möglich");
                    }
                }

                @Override
                public void onFailure(Call<Medium> call, Throwable throwable) {
                    showError("No Connection");
                }
            });
        } else {
            api.createMedia(medium).enqueue(new Callback<Medium>() {
                @Override
                public void onResponse(Call<Medium> call, Response<Medium> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(MediaManageActivity.this, MainActivity.class));
                    } else {
                        showError("Erstellen des Mediums nicht möglich");
                    }
                }

                @Override
                public void onFailure(Call<Medium> call, Throwable throwable) {
                            showError("No Connection");
                }
            });
        }


    }

    /**
     * Erstellt ein Medium-Objekt aus den aktuellen Eingabefeldern.
     *
     * @return das zusammengestellte Medium-Objekt
     */
    private Medium getMedium() {
        Medium medium = new Medium(title.getText().toString(), author.getText().toString());
        medium.setEan(!isbn.getText().toString().trim().isEmpty() ? Long.valueOf(isbn.getText().toString()) : null);
        medium.setFsk(!min_age.getText().toString().trim().isEmpty()  ? Short.valueOf(min_age.getText().toString()) : null);
        medium.setGenre(genre.getText() != null ? genre.getText().toString() : null);
        medium.setStandort(location_code.getText() != null ? location_code.getText().toString() : null);

        return medium;
    }

    /**
     * Zeigt einen Fehlerdialog mit der übergebenen Nachricht an.
     *
     * @param message die anzuzeigende Fehlermeldung
     */
    private void showError(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Fehler")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}