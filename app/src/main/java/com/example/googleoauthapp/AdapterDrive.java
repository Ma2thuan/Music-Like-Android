package com.example.googleoauthapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.OptIn;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Song_Dri;

import java.util.List;

@UnstableApi public class AdapterDrive extends RecyclerView.Adapter<AdapterDrive.SongViewHolder> {
    private List<Song_Dri> songs;
    private SimpleExoPlayer player;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Song_Dri song);
    }

    public AdapterDrive(List<Song_Dri> songs, OnItemClickListener onItemClickListener) {
        this.songs = songs;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_song, parent, false);
        return new SongViewHolder(view);
    }

    @OptIn(markerClass = UnstableApi.class) @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        Song_Dri song = songs.get(position);
        holder.songName.setText(song.getName());
        /*holder.itemView.setOnClickListener(v -> {
            onItemClickListener.onItemClick(song);
            Intent intent = new Intent(v.getContext(), screen_now_upload.class);
            intent.putExtra("SONG_URL", song.getUrl());
            intent.putExtra("SONG_NAME", song.getName());
            v.getContext().startActivity(intent);
        });*/
        holder.itemView.setOnClickListener(v -> {
            // Dừng và giải phóng ExoPlayer hiện tại
            if (player != null) {
                player.setPlayWhenReady(false); // Dừng phát nhạc
                player.release(); // Giải phóng tài nguyên
                player = null; // Đặt lại player thành null
            }

            // Tạo Intent để chuyển đến ScreenNowUploadActivity
            Intent intent = new Intent(v.getContext(), screen_now_upload.class);
            intent.putExtra("SONG_URL", song.getUrl());
            intent.putExtra("SONG_NAME", song.getName());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songName;

        public SongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.title);
        }
    }
}
