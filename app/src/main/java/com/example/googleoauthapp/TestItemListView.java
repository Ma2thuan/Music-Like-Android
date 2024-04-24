package com.example.googleoauthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.googleoauthapp.Adapter.MusicAdapterTesy;
import com.example.googleoauthapp.Adapter.MusicApdater;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.Class.SongTest;
import com.example.googleoauthapp.Connectors.SongService;
import com.example.googleoauthapp.databinding.ActivityTestItemListViewBinding;

import java.util.ArrayList;

public class TestItemListView extends AppCompatActivity {

    ActivityTestItemListViewBinding binding;

    MusicAdapterTesy adapter;
    SongService service;
    ArrayList<SongTest> songs;
    ArrayList<SongTest> takeSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestItemListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = new SongService(getApplicationContext());

        //loadData();
    }

    private void loadData() {
        songs = new ArrayList<SongTest>();

            service.getTheStrokes(() -> {
                takeSongs = service.getSongsTest();
                if (takeSongs!= null &&!takeSongs.isEmpty()) {
                    // Add a new song to songs
                    for (int i = 0; i < takeSongs.size(); i++) {
                        songs.add(i, new SongTest(takeSongs.get(i).getId(),takeSongs.get(i).getName() , R.drawable.imv1));

                        Log.d("TAG2", songs.get(i).getName() + " " + songs.get(i).getId());
                    }

                    // Update the adapter with the new songs list
                    if (adapter!= null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new MusicAdapterTesy(TestItemListView.this, R.layout.item_list_music, songs);
                        binding.lvSongList.setAdapter(adapter);
                    }
                }
            });
        }

    }


