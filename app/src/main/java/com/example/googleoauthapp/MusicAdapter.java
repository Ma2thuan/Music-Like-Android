package com.example.googleoauthapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> songList;

    public MusicAdapter(ArrayList<HashMap<String, String>> songList) {
        this.songList = songList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> song = songList.get(position);
        holder.songTitle.setText(song.get("songTitle"));
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    private void playMusic(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync(); // Chuẩn bị MediaPlayer một cách bất đồng bộ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);

            // Set the click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the position of the clicked item
                    int position = getBindingAdapterPosition();
                    // Get the song path from the songList using the position
                    String songPath = songList.get(position).get("songPath");
                    Intent intent = new Intent(view.getContext(), screen_now_playing.class);
                    intent.putExtra("SONG_PATH", songPath);
                    intent.putExtra("SONG_LIST", songList);
                    intent.putExtra("SONG_INDEX", position);
                    view.getContext().startActivity(intent);
                }
            });
        }
        private void playMusic(String path) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}


