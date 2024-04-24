package com.example.googleoauthapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Song;
import com.example.googleoauthapp.SongsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Playlist_Songs extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SongsAdapter adapter;
    private ArrayList<Song> songList;
    private String playlistName;
/*    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_songs);
        // Khởi tạo RecyclerView và Adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo Adapter với danh sách rỗng và SongItemClickListener
        adapter = new SongsAdapter(new ArrayList<>(), (songList, position) -> {
            Intent intent = new Intent(Playlist_Songs.this, screen_now_playing_ex.class);
            intent.putExtra("SONG_LIST", songList); // Truyền songList đã được cập nhật
            intent.putExtra("SONG_INDEX", position);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        String playlistName = getIntent().getStringExtra("PLAYLIST_NAME");
        if (playlistName != null) {
            loadSongsFromPlaylist(playlistName);
        }
    }*/
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_playlist_songs);
    // Khởi tạo RecyclerView và Adapter
    recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));

    // Lấy tên playlist từ Intent
    String playlistName = getIntent().getStringExtra("PLAYLIST_NAME");

    // Khởi tạo Adapter với danh sách rỗng, SongItemClickListener và tên playlist
    adapter = new SongsAdapter(new ArrayList<>(), (songList, position) -> {
        Intent intent = new Intent(Playlist_Songs.this, screen_now_playing_ex.class);
        intent.putExtra("SONG_LIST", songList); // Truyền songList đã được cập nhật
        intent.putExtra("SONG_INDEX", position);
        startActivity(intent);
    }, playlistName); // Thêm tham số playlistName vào đây

    recyclerView.setAdapter(adapter);

    // Nếu tên playlist không null, load bài hát từ playlist
    if (playlistName != null) {
        loadSongsFromPlaylist(playlistName);
    }
}

    private void loadSongsFromPlaylist(String playlistId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String useremail = GlobalVars.getUserEmail();
        if (useremail != null) {
            db.collection("users").document(useremail).collection("playlists").document(playlistId).collection("songs")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                /*songList.clear();//new*/
                                List<Song> songs = new ArrayList<>();
                                for (QueryDocumentSnapshot songDocument : task.getResult()) {
                                    String songName = songDocument.getId(); // Tên của document là tên bài hát
                                    String songPath = songDocument.getString("songPath");
                                    songs.add(new Song(songName, songPath));

                                    Log.d("Playlist_Songs", "Song name: " + songName + ", songPath: " + songPath);
                                }
                                // Cập nhật RecyclerView với danh sách bài hát
                                updateRecyclerView(songs);
                            } else {
                                Log.w(TAG, "Error getting songs.", task.getException());
                            }
                        }
                    });
        } else {
            Log.w(TAG, "User email is null");
        }
    }

    private void updateRecyclerView(List<Song> songs) {
        SongsAdapter adapter = (SongsAdapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setSongs(songs);
            adapter.notifyDataSetChanged();}
}}