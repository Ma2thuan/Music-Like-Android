package com.example.googleoauthapp;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.googleoauthapp.ui.dashboard.DashboardViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class screen_now_playing extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();
    private ArrayList<HashMap<String, String>> songList;
    private int currentSongIndex = 0;
    boolean isPlaying = false; // Biến theo dõi trạng thái phát nhạc

    private SeekBar songProgressBar;
    private TextView currentDurationView;
    private TextView totalDurationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_now_playing);
        FirebaseApp.initializeApp(this);

        songProgressBar = findViewById(R.id.song_progressbar);
        currentDurationView = findViewById(R.id.song_current_duration);
        totalDurationView = findViewById(R.id.song_total_duration);

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
                finish();
                }
        });
        // sekbar
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    currentDurationView.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mediaPlayer.getDuration();
                int currentPosition = seekBar.getProgress();
                mediaPlayer.seekTo(currentPosition);
                updateProgressBar();
            }
        });
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
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

                    updateProgressBar();

                    mHandler.postDelayed(mUpdateTimeTask, 1000);

                    // Cập nhật tên bài hát lên TextView
                    String songTitle = songList.get(currentSongIndex).get("songTitle");
                    TextView titleTextView = findViewById(R.id.title);
                    if (titleTextView != null) {
                        titleTextView.setText(songTitle);}
                    titleTextView.setSelected(true);

                    // Cập nhật thời gian tổng cộng
                    int totalDuration = mediaPlayer.getDuration();
                    totalDurationView.setText(milliSecondsToTimer(totalDuration));
                    // Cập nhật thanh tiến độ
                    songProgressBar.setMax(totalDuration);

                }
            });

            //sau khi phat het bai hat, chuyen sang bai tiep theo trong danh sach
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Kiểm tra nếu không phải bài cuối cùng trong danh sách
                    if (currentSongIndex < songList.size() - 1) {
                        currentSongIndex++;
                        playMusic(songList.get(currentSongIndex).get("songPath"));
                    } else {
                        // Nếu là bài cuối, quay lại bài đầu tiên hoặc dừng phát
                        currentSongIndex = 0;
                        playMusic(songList.get(currentSongIndex).get("songPath"));
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Handler mHandler = new Handler();
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long currentDuration = mediaPlayer.getCurrentPosition();
            currentDurationView.setText(milliSecondsToTimer(currentDuration));
            songProgressBar.setProgress((int)currentDuration);
            mHandler.postDelayed(this, 1000);
        }
    };

    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString;

        // Chuyển đổi tổng thời gian thành thời gian phát
        int hours = (int)(milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int)((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Thêm giờ nếu có
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        finalTimerString += (minutes < 10 ? "0" : "") + minutes + ":";
        secondsString = (seconds < 10 ? "0" : "") + seconds;
        finalTimerString += secondsString;
        // trả về định dạng
        return finalTimerString;
    }
    // Đảm bảo giải phóng MediaPlayer khi không cần thiết nữa
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mUpdateTimeTask);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
}
