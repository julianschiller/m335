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

import com.baloise.library.common.Ausleihe;
import com.baloise.library.common.Medium;
import com.baloise.library.service.BorrowingApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowingManageActivity extends AppCompatActivity {

    private TextInputEditText customerId;
    private TextInputEditText invNmr;
    private TextInputEditText borrowed;
    private TextInputEditText returnUntil;
    private Button save;
    private Button cancel;

    private TextInputLayout customerLayout;
    private TextInputLayout mediaLayout;
    private boolean isCreate;
    private Long id;

    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    BorrowingApi api = RetrofitFactory.getRetrofitInstance().create(BorrowingApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_borrowing_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isCreate = getIntent().getBooleanExtra("create", false);
        id = getIntent().getLongExtra("media_id", -1);

        customerId = findViewById(R.id.input_customer_id);
        invNmr = findViewById(R.id.input_inv_nmr);

        borrowed = findViewById(R.id.input_borrowed_date);
        borrowed.setEnabled(false);

        returnUntil = findViewById(R.id.input_return_date);

        cancel = findViewById(R.id.cancel_borrowing);
        cancel.setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(view.getContext(), ShowBorrowingActivity.class));
        });

        save = findViewById(R.id.save_borrowing);
        customerLayout = findViewById(R.id.customer_layout);
        mediaLayout = findViewById(R.id.media_layout);

        if (isCreate) {
            createBorrowing();
        } else {
            loadBorrowing(id);
            extendBorrowing(id);
        }


    }

    private void loadBorrowing(Long id) {
        api.getBorrowing(id).enqueue(new Callback<Ausleihe>() {
            @Override
            public void onResponse(Call<Ausleihe> call, Response<Ausleihe> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Ausleihe ausleihe = response.body();
                    customerId.setText(String.valueOf(ausleihe.getKunde().getId()));
                    invNmr.setText(String.valueOf(ausleihe.getMedium().getId()));
                    borrowed.setText(ausleihe.getLeihdatumLokalisiert());
                    returnUntil.setText(ausleihe.getFaelligkeitsdatumLokalisiert());
                } else {
                    showError("Laden der Ausleihe fehlgeschlagen");
                }
            }

            @Override
            public void onFailure(Call<Ausleihe> call, Throwable throwable) {
                showError("No Connection");
            }
        });

        customerId.setEnabled(false);
        invNmr.setEnabled(false);
    }

    private void extendBorrowing(Long id) {
        save.setText("Verlängern");
        save.setOnClickListener(view -> {
            api.extendBorrowing(id).enqueue(new Callback<Ausleihe>() {
                @Override
                public void onResponse(Call<Ausleihe> call, Response<Ausleihe> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(BorrowingManageActivity.this, ShowBorrowingActivity.class));
                    } else {
                        showError("Verlängern nicht möglich");
                    }
                }

                @Override
                public void onFailure(Call<Ausleihe> call, Throwable throwable) {
                    showError("No Connection");
                }
            });
        });
    }

    private void createBorrowing() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 14);

        borrowed.setText(format.format(new Date()));
        returnUntil.setText(format.format(cal.getTime()));

        save.setOnClickListener(view -> {
            if (!isValid()) {
                return;
            }
            Ausleihe borrowing = new Ausleihe(Long.valueOf(customerId.getText().toString()), Long.valueOf(invNmr.getText().toString()));

            api.createBorrowing(borrowing).enqueue(new Callback<Ausleihe>() {
                @Override
                public void onResponse(Call<Ausleihe> call, Response<Ausleihe> response) {
                    if (response.isSuccessful()) {
                        startActivity(new Intent(BorrowingManageActivity.this, ShowBorrowingActivity.class));
                    } else {
                        showError("Ausleihe nicht möglich");
                    }
                }

                @Override
                public void onFailure(Call<Ausleihe> call, Throwable throwable) {
                    showError("No Connection");
                }
            });
        });
    }

    private boolean isValid() {
        boolean valid = true;
        String customerIdText = customerId.getText().toString().trim();
        String mediaIdText = invNmr.getText().toString().trim();

        if (customerIdText.isEmpty()) {
            customerLayout.setError("Customer ID ist erforderlich");
            valid = false;
        } else if (!customerIdText.matches("\\d+")){
            customerLayout.setError("Customer ID muss eine Zahl sein");
            valid = false;
        } else {
            customerLayout.setError(null);
        }

        if (mediaIdText.isEmpty()) {
            mediaLayout.setError("Medien ID ist erforderlich");
            valid = false;
        } else if (!mediaIdText.matches("\\d+")) {
            mediaLayout.setError("Medien ID muss eine Zahl sein");
            valid = false;
        } else {
            mediaLayout.setError(null);
        }

        return valid;
    }

    private void showError(String message) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Fehler")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
