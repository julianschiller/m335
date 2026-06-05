package com.baloise.library;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBorrowingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BorrowingAdapter adapter;
    private List<Ausleihe> borrowings = new ArrayList<>();

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

        loadBorrowings();
    }

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
                    Toast.makeText(ShowBorrowingActivity.this, "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Ausleihe>> call, Throwable throwable) {
                Toast.makeText(ShowBorrowingActivity.this, "No Connection: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
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
}