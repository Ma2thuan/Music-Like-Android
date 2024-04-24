package com.example.googleoauthapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Playlist;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<Playlist> playlists;

    public PlaylistAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }
    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_child, parent, false);
        return new PlaylistViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.titleTextView.setText(playlist.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Playlist_Songs.class);
                String playlistName = playlists.get(holder.getAdapterPosition()).getName(); // Giả sử Playlist có phương thức getId()
                intent.putExtra("PLAYLIST_NAME", playlistName);
                v.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        public PlaylistViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
