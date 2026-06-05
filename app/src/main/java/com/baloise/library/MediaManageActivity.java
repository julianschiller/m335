package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.baloise.library.common.Medium;
import com.baloise.library.service.MediaApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaManageActivity extends AppCompatActivity {

    private TextInputEditText title;
    private TextInputEditText author;
    private TextInputEditText genre;
    private TextInputEditText isbn;
    private TextInputEditText location_code;
    private TextInputEditText min_age;
    private Button save;
    private Button cancel;

    private boolean isCreate;
    private Long id;

    MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

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
        cancel.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);

            view.getContext().startActivity(intent);
        });

        if (isCreate) {

        } else {
            loadMedia(id);
            editMedia();
        }
    }

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
                    Toast.makeText(MediaManageActivity.this, "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Medium> call, Throwable throwable) {
                Toast.makeText(MediaManageActivity.this,
                        "No Connection: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void editMedia() {
        title.setEnabled(false);
        author.setEnabled(false);

        save.setOnClickListener(view -> {

            Medium medium = new Medium(title.getText().toString(), author.getText().toString());
            medium.setEan(!isbn.getText().toString().trim().isEmpty() ? Long.valueOf(isbn.getText().toString()) : null);
            medium.setFsk(!min_age.getText().toString().trim().isEmpty()  ? Short.valueOf(min_age.getText().toString()) : null);
            medium.setGenre(genre.getText() != null ? genre.getText().toString() : null);
            medium.setStandort(location_code.getText() != null ? location_code.getText().toString() : null);

            saveMedia(id, medium);

        });
    }

    private void saveMedia(Long id, Medium medium) {
        if (id != null) {
            api.editMedia(id, medium).enqueue(new Callback<Medium>() {
                @Override
                public void onResponse(Call<Medium> call, Response<Medium> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(MediaManageActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(MediaManageActivity.this, "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Medium> call, Throwable throwable) {
                    Toast.makeText(MediaManageActivity.this,
                            "No Connection: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {

        }


    }
}