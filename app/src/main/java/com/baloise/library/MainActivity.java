package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Hauptaktivität zur Anzeige und Verwaltung der Medienliste mit Sortierfunktion.
 *
 * @author Julian Schiller
 */
public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MediaAdapter adapter;
    private List<Medium> medias = new ArrayList<>();
    private List<Medium> originalMedias = new ArrayList<>();
    private Button createNew;
    private Spinner sortCrit;
    private Spinner sortDirection;

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

        createNew = findViewById(R.id.add_media);
        createNew.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MediaManageActivity.class);

            intent.putExtra("create", true);

            view.getContext().startActivity(intent);
        });

        sortCrit = findViewById(R.id.spinner_sort_field);
        sortDirection = findViewById(R.id.spinner_sort_direction);

        sortCrit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortMedias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sortDirection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortMedias();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

    /**
     * Lädt alle Medien vom Backend und aktualisiert die Liste.
     */
    private void loadMedias() {
        MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

        api.getMedias().enqueue(new Callback<List<Medium>>() {
            @Override
            public void onResponse(@NonNull Call<List<Medium>> call, @NonNull Response<List<Medium>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    medias.clear();
                    medias.addAll(response.body());
                    originalMedias.clear();
                    originalMedias.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    showError("Laden der Medien nicht möglich");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Medium>> call, @NonNull Throwable throwable) {
                showError("No Connection");
            }
        });
    }

    /**
     * Sortiert die Medienliste nach dem gewählten Kriterium und der Richtung.
     */
    private void sortMedias() {
        String field = sortCrit.getSelectedItem().toString();
        String direction = sortDirection.getSelectedItem().toString();
        boolean descending = direction.equals("Absteigend");

        if (!field.equals("Autor") && !field.equals("Titel")) {
            medias.clear();
            medias.addAll(originalMedias);
            adapter.notifyDataSetChanged();
            return;
        }

        Collections.sort(medias, new Comparator<Medium>() {
            @Override
            public int compare(Medium a, Medium b) {
                int result;
                if (field.equals("Autor")) {
                    result = a.getAutor().compareToIgnoreCase(b.getAutor());
                } else {
                    result = a.getTitel().compareToIgnoreCase(b.getTitel());
                }
                return descending ? -result : result;
            }
        });

        adapter.notifyDataSetChanged();
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