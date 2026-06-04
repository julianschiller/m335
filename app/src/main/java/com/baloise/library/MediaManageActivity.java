package com.baloise.library;

import android.os.Bundle;

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

        title = findViewById(R.id.input_title);
    }

    private void loadMedia(int id) {
        MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

        api.getMedia(id).enqueue(new Callback<Medium>() {
            @Override
            public void onResponse(Call<Medium> call, Response<Medium> response) {

            }

            @Override
            public void onFailure(Call<Medium> call, Throwable throwable) {

            }
        });
    }
}