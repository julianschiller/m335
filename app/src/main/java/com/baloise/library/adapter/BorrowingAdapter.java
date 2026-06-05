package com.baloise.library.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baloise.library.BorrowingManageActivity;
import com.baloise.library.MediaManageActivity;
import com.baloise.library.R;
import com.baloise.library.common.Ausleihe;
import com.baloise.library.common.Medium;
import com.baloise.library.service.MediaApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BorrowingAdapter extends RecyclerView.Adapter<BorrowingAdapter.BorrowingViewHolder> {

    private List<Ausleihe> borrowings;

    public BorrowingAdapter(List<Ausleihe> borrowings) {
        this.borrowings = borrowings;
    }

    public static class BorrowingViewHolder extends RecyclerView.ViewHolder {

        private TextView customerId;
        private TextView customerName;
        private TextView invNmbr;
        private TextView mediaName;
        private TextView returnDate;
        private ImageButton deleteButton;

        public BorrowingViewHolder(View itemView) {
            super(itemView);

            customerId = itemView.findViewById(R.id.customer_id);
            customerName = itemView.findViewById(R.id.customer_name);
            invNmbr = itemView.findViewById(R.id.item_inv_number);
            mediaName = itemView.findViewById(R.id.media_name);
            returnDate = itemView.findViewById(R.id.item_return_date);
            deleteButton = itemView.findViewById(R.id.item_borrowing_delete);

        }

        public TextView getInvNmbr() {
            return invNmbr;
        }

        public TextView getCustomerId() {
            return customerId;
        }

        public TextView getCustomerName() {
            return customerName;
        }

        public TextView getMediaName() {
            return mediaName;
        }

        public TextView getReturnDate() {
            return returnDate;
        }

        public ImageButton getDeleteButton() {
            return deleteButton;
        }
    }

    @NonNull
    @Override
    public BorrowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.borrowing_list_layout, parent, false);

        return new BorrowingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(BorrowingViewHolder holder, int position) {
        Ausleihe a = borrowings.get(position);
        holder.invNmbr.setText(String.valueOf(a.getMedium().getId()));
        holder.mediaName.setText(a.getMedium().getTitel());
        holder.customerId.setText(String.valueOf(a.getKunde().getId()));
        holder.customerName.setText(a.getKunde().getVorname() + " " + a.getKunde().getFamilienname());
        holder.returnDate.setText(a.getFaelligkeitsdatumLokalisiert());
/*
        holder.deleteButton.setOnClickListener(view -> {
            String message = "Medium \"" + m.getTitel() + "\" sicher löschen?";
            new MaterialAlertDialogBuilder(view.getContext())
                    .setTitle("Medium löschen")
                    .setMessage(message)
                    .setPositiveButton("Ja", (dialog, which) -> {
                        MediaApi api = RetrofitFactory.getRetrofitInstance().create(MediaApi.class);

                        api.deleteMedia(m.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    int pos = holder.getBindingAdapterPosition();
                                    if (pos != RecyclerView.NO_POSITION) {
                                        medien.remove(pos);
                                        notifyItemRemoved(pos);
                                    }
                                } else {
                                    Toast.makeText(view.getContext(),
                                            "Fehler: " + response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable throwable) {
                                Toast.makeText(view.getContext(),
                                        "No Connection: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    })
                    .setNegativeButton("Nein", null)
                    .show();
        }); */

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), BorrowingManageActivity.class);

            intent.putExtra("media_id", a.getMedium().getId());
            intent.putExtra("create", false);

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return borrowings.size();
    }
}
