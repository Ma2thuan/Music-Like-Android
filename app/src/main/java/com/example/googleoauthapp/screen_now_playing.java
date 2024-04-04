package com.example.googleoauthapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googleoauthapp.ui.dashboard.DashboardViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class screen_now_playing extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<HashMap<String, String>> songList;
    private int currentSongIndex = 0;
    boolean isPlaying = false; // Biến theo dõi trạng thái phát nhạc

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_now_playing);

        // Lấy danh sách bài hát và vị trí bài hát hiện tại từ Intent
        songList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("SONG_LIST");
        currentSongIndex = getIntent().getIntExtra("SONG_INDEX", 0);

        // Phát bài hát hiện tại
        playMusic(songList.get(currentSongIndex).get("songPath"));

        // Thiết lập các nút điều khiển
        setupControls();
    }
    private void setupControls() {
        ImageView playPauseButton = findViewById(R.id.play_pause_button);
        // Các listener cho playPauseButton như trước
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái và thay đổi hình ảnh và trạng thái phát nhạc
                if (isPlaying) {
                    mediaPlayer.pause(); // Dừng phát nhạc
                    playPauseButton.setImageResource(R.drawable.ic_play_white); // Thay đổi hình ảnh thành icon play
                    isPlaying = false;
                } else {
                    mediaPlayer.start(); // Bắt đầu phát nhạc
                    playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp); // Thay đổi hình ảnh thành icon pause
                    isPlaying = true;
                }
            }
        });

        ImageView prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang bài hát phía trước
                if (currentSongIndex > 0) {
                    currentSongIndex--;
                    playMusic(songList.get(currentSongIndex).get("songPath"));
                }
            }
        });

        ImageView nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang phát bài hát tiếp theo
                if (currentSongIndex < songList.size() - 1) {
                    currentSongIndex++;
                    playMusic(songList.get(currentSongIndex).get("songPath"));
                }
            }
        });

        ImageView backButton = findViewById(R.id.button_right);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DashboardViewModel.class);
                view.getContext().startActivity(intent);
                }
        });
    }

    private void playMusic(String path) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                    isPlaying = true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Đảm bảo giải phóng MediaPlayer khi không cần thiết nữa
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }
}
