package com.example.googleoauthapp;

import static android.content.ContentValues.TAG;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Song_Dri;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@UnstableApi public class ActDrive extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SimpleExoPlayer player;
    private String userAgent = "GoogleOAuthApp/1.0 (Android)"; // Thông tin user-agent

    private void playSong(String url) {
        if (player != null) {
            player.release();
        }
        player = new SimpleExoPlayer.Builder(this).build();
        MediaSource mediaSource = buildMediaSource(Uri.parse(url));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true); // Phát nhạc ngay khi đã sẵn sàng
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent(userAgent);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actdrive);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AdapterDrive adapter = new AdapterDrive(new ArrayList<>(), song -> playSong(song.getUrl()));
        recyclerView.setAdapter(adapter);
        loadSongsFromStorage();
    }

    // Phương thức loadSongsFromStorage với AdapterDrive
    private void loadSongsFromStorage() {
        String userEmail = GlobalVars.getUserEmail();
        if (userEmail != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            StorageReference userFolder = storageRef.child("users/" + userEmail.replace('.', '_'));
            List<Song_Dri> songList = new ArrayList<>();
            AdapterDrive adapter = new AdapterDrive(songList, song -> playSong(song.getUrl()));
            recyclerView.setAdapter(adapter);

            userFolder.listAll()
                    .addOnSuccessListener(listResult -> {
                        for (StorageReference item : listResult.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(uri -> {
                                Log.d(TAG, "Found song: " + uri.toString());
                                songList.add(new Song_Dri(item.getName(), uri.toString()));
                                adapter.notifyDataSetChanged();
                            }).addOnFailureListener(e -> {
                                Log.w(TAG, "Error getting download URL", e);
                            });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error listing songs", e);
                    });
        } else {
            Log.w(TAG, "User email is null");
        }
    }
}