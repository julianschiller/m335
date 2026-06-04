package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baloise.library.adapter.MediaAdapter;
import com.baloise.library.common.Medium;
import com.baloise.library.service.MediaApi;
import com.baloise.library.service.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MediaAdapter adapter;
    private List<Medium> medias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.media_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MediaAdapter(medias);
        recyclerView.setAdapter(adapter);

        loadMedias();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mnu_media) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (item.getItemId() == R.id.mnu_borrowing) {
            startActivity(new Intent(this, ShowBorrowingActivity.class));
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void loadMedias() {
        MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

        api.getMedia().enqueue(new Callback<List<Medium>>() {
            @Override
            public void onResponse(@NonNull Call<List<Medium>> call, @NonNull Response<List<Medium>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    medias.clear();
                    medias.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Medium>> call, @NonNull Throwable throwable) {
                Toast.makeText(MainActivity.this,
                        "No Connection: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}