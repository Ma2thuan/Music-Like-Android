package com.example.googleoauthapp;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Media;

import com.example.googleoauthapp.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity3 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private ArrayList<HashMap<String, String>> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Lấy danh sách bài hát từ thiết bị
        songList = getMusicFiles();
        musicAdapter = new MusicAdapter(songList);
        recyclerView.setAdapter(musicAdapter);
    }

    public ArrayList<HashMap<String, String>> getMusicFiles() {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        // Uri for external music files
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        // Projection for the columns we want to retrieve
        String[] projection = {
                Media._ID,
                Media.TITLE,
                Media.DATA,
        };
        // Query the content resolver
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> song = new HashMap<>();
                song.put("songTitle", cursor.getString(cursor.getColumnIndexOrThrow(Media.TITLE)));
                song.put("songPath", cursor.getString(cursor.getColumnIndexOrThrow(Media.DATA)));
                fileList.add(song);
            }
            cursor.close();
        }
        return fileList;
    }
}
