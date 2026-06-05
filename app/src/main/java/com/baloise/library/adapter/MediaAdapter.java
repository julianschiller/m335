package com.baloise.library.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baloise.library.MediaManageActivity;
import com.baloise.library.R;
import com.baloise.library.common.Medium;
import com.baloise.library.service.MediaApi;
import com.baloise.library.service.RetrofitFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.MediaViewHolder> {

    private List<Medium> medien;

    public MediaAdapter(List<Medium> medien) {
        this.medien = medien;
    }

    public static class MediaViewHolder extends RecyclerView.ViewHolder {

        private TextView invNmbr;
        private TextView title;
        private TextView author;
        private ImageButton deleteButton;

        public MediaViewHolder(View itemView) {
            super(itemView);

            invNmbr = itemView.findViewById(R.id.item_inv_nr);
            title = itemView.findViewById(R.id.item_title);
            author = itemView.findViewById(R.id.item_author);
            deleteButton = itemView.findViewById(R.id.item_media_delete);

        }

        public TextView getInvNmbr() {return invNmbr; }
        public TextView getTitle() {return title; }
        public TextView getAuthor() {return author; }
        public ImageButton getDeleteButton() {return deleteButton; }
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.media_list_layout, parent, false);

        return new MediaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaViewHolder holder, int position) {
        Medium m = medien.get(position);
        holder.invNmbr.setText(String.valueOf(m.getId()));
        holder.author.setText(m.getAutor());
        holder.title.setText(m.getTitel());

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
        });

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MediaManageActivity.class);

            intent.putExtra("medium_id", m.getId());
            intent.putExtra("create", false);

            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medien.size();
    }
}
