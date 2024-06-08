package com.example.googleoauthapp;

import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DefaultHttpDataSource;
import androidx.media3.exoplayer.SimpleExoPlayer;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;

@UnstableApi public class screen_now_upload extends AppCompatActivity {
    private SimpleExoPlayer player;
    private TextView songTitleTextView;
    private String userAgent = "GoogleOAuthApp/1.0 (Android)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_now_upload);

        player = new SimpleExoPlayer.Builder(this).build();
        songTitleTextView = findViewById(R.id.title);

        String songUrl = getIntent().getStringExtra("SONG_URL");
        String songName = getIntent().getStringExtra("SONG_NAME");

        if (songTitleTextView != null) {
            songTitleTextView.setText(songName);
        }

        MediaSource mediaSource = buildMediaSource(Uri.parse(songUrl));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent(userAgent);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
    }

    // Đừng quên override phương thức onDestroy để giải phóng tài nguyên
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}