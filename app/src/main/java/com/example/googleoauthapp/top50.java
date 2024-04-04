package com.example.googleoauthapp;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googleoauthapp.Adapter.MusicApdater;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.databinding.ActivityTop50Binding;

import java.util.ArrayList;

public class top50 extends AppCompatActivity {
    ActivityTop50Binding binding;
    ListView lvListMusic;

    MusicApdater musicApdater;

    ArrayList<Song> songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityTop50Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        lvListMusic = findViewById(R.id.lvListMusic);
        loadData();
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadData() {
        songs = new ArrayList<>();
        songs.add(new Song("123","Nhac dui",R.drawable.imv3));

        musicApdater = new MusicApdater(top50.this, R.layout.item_list_music,songs);

        songs.add(new Song("abc","Nhac buồn",R.drawable.imv4));

        musicApdater = new MusicApdater(top50.this, R.layout.item_list_music,songs);

        lvListMusic.setAdapter(musicApdater);

    }
}