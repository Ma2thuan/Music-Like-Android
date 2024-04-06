package com.example.googleoauthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.googleoauthapp.Adapter.MusicApdater;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.Connectors.SongService;
import com.example.googleoauthapp.databinding.ActivityTestItemListViewBinding;

import java.util.ArrayList;

public class TestItemListView extends AppCompatActivity {

    ActivityTestItemListViewBinding binding;

    MusicApdater adapter;
    SongService service;
    ArrayList<Song> songs;
    ArrayList<Song> takeSongs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestItemListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = new SongService(getApplicationContext());

        loadData();
    }

    private void loadData() {
        songs = new ArrayList<>();

//        service.getRecentlyPlayedTracks(() -> {
//            takeSongs = service.getSongs();
//            Log.d("TAG2" , String.valueOf(takeSongs.size()));
//            Log.d("TAG2" , takeSongs.get(0).getName());
//
//        });
//
//            //Log.d("TAG2" , takeSongs.get(0).getId());
//
//            takeSongs.add(new Song(takeSongs.get(0).getId() , takeSongs.get(0).getName() , R.drawable.imv1));
//
//            Log.d("TAG2" , takeSongs.get(0).getId());


            service.getRecentlyPlayedTracks(() -> {
                takeSongs = service.getSongs();
                if (takeSongs!= null &&!takeSongs.isEmpty()) {
                    // Add all elements from takeSongs to songs
                    songs.addAll(takeSongs);

                    // Add a new song to songs
                    for (int i = 0; i < takeSongs.size(); i++) {
                        songs.add(i, new Song(takeSongs.get(i).getId(),takeSongs.get(i).getName() , R.drawable.imv1));

                        Log.d("TAG2", songs.get(i).getName() + " " + songs.get(i).getId());
                    }

                    // Update the adapter with the new songs list
                    if (adapter!= null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new MusicApdater(TestItemListView.this, R.layout.item_list_music, songs);
                        binding.lvSongList.setAdapter(adapter);
                    }
                }
            });
        }


//        Log.d("TAG2", songs.get(15).getName() + songs.get(15).getId());
//
//        new Song("123" , "Cool Song" ,R.drawable.imv1 ));
//        songs.add(30 , new Song("1234" , "Cool Song2" ,R.drawable.imv1 ));
//        songs.add( 50, new Song("12356" , "Cool Song" ,R.drawable.imv1 ));
//
//        Log.d("SIZE1" , String.valueOf(songs.size()));
//
//        adapter = new MusicApdater(TestItemListView.this , R.layout.item_list_music ,songs);
//
//        binding.lvSongList.setAdapter(adapter);

    }


