package com.example.googleoauthapp.ui.playlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.googleoauthapp.Adapter.MusicApdater;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.Connectors.SongService;
import com.example.googleoauthapp.R;
import com.example.googleoauthapp.databinding.ActivityTop100Binding;

import java.util.ArrayList;


public class top50VN extends AppCompatActivity {

    ActivityTop100Binding binding;

    MusicApdater adapter;
    SongService service;
    ArrayList<Song> songs;
    ArrayList<Song> takeSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTop100Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        service = new SongService(getApplicationContext());

        loadData();
    }

    private void loadData() {
        songs = new ArrayList<Song>();
        Log.i("test", "ok");

        songs.clear();

        service.getTop50VN(() -> {
            takeSongs = service.getSongs();
            if (takeSongs!= null &&!takeSongs.isEmpty()) {
                // Add a new song to songs
                for (int i = 0; i < takeSongs.size(); i++) {
                    songs.add(i, new Song(takeSongs.get(i).getId(),takeSongs.get(i).getName() , R.drawable.region_vn_default));

                    Log.d("TAG2", songs.get(i).getName() + " " + songs.get(i).getId());
                }

                // Update the adapter with the new songs list
                if (adapter!= null) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter = new MusicApdater(top50VN.this, R.layout.item_list_music, songs);
                    binding.lvListMusic.setAdapter(adapter);
                }
            }
        });
    }


}