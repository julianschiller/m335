package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baloise.library.adapter.BorrowingAdapter;
import com.baloise.library.common.Ausleihe;
import com.baloise.library.service.BorrowingApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity zur Anzeige aller Ausleihen in einer Liste.
 *
 * @author Julian Schiller
 */
public class ShowBorrowingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowingAdapter adapter;
    private List<Ausleihe> borrowings = new ArrayList<>();
    private Button createNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_borrowing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        recyclerView = findViewById(R.id.borrowing_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new BorrowingAdapter(borrowings);
        recyclerView.setAdapter(adapter);

        createNew = findViewById(R.id.add_borrowing);
        createNew.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BorrowingManageActivity.class);

            intent.putExtra("create", true);
            view.getContext().startActivity(intent);
        });

        loadBorrowings();
    }

    /**
     * Lädt alle Ausleihen vom Backend und aktualisiert die Liste.
     */
    private void loadBorrowings() {
        BorrowingApi api = RetrofitFactory.getRetrofitInstance().create(BorrowingApi.class);

        api.getBorrowings().enqueue(new Callback<List<Ausleihe>>() {
            @Override
            public void onResponse(Call<List<Ausleihe>> call, Response<List<Ausleihe>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    borrowings.clear();
                    borrowings.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    showError("Laden der Ausleihen nicht möglich");
                }

            }

            @Override
            public void onFailure(Call<List<Ausleihe>> call, Throwable throwable) {
                showError("No Connection");
            }
        });

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