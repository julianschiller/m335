package com.baloise.library.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baloise.library.R;
import com.baloise.library.common.Medium;

import java.util.List;

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

        });

        holder.itemView.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return medien.size();
    }
}
