package com.example.googleoauthapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.googleoauthapp.model.Song;
import com.example.googleoauthapp.ui.dashboard.DashboardViewModel;
import com.google.firebase.FirebaseApp;


import java.io.IOException;
import java.util.ArrayList;

public class screen_now_playing_ex extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ArrayList<Song> songList;
    private int currentSongIndex = 0;
    boolean isPlaying = false;
    private SeekBar songProgressBar;
    private TextView currentDurationView;
    private TextView totalDurationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_now_playing_ex);
        FirebaseApp.initializeApp(this);

        mediaPlayer = new MediaPlayer();
        Intent intent = getIntent();

        songProgressBar = findViewById(R.id.song_progressbar);
        currentDurationView = findViewById(R.id.song_current_duration);
        totalDurationView = findViewById(R.id.song_total_duration);


        songList = (ArrayList<Song>) intent.getSerializableExtra("SONG_LIST");
        currentSongIndex = getIntent().getIntExtra("SONG_INDEX", 0);
        playMusic(songList.get(currentSongIndex).getPath());

        boolean shuffleEnabled = getIntent().getBooleanExtra("SHUFFLE_ENABLED", false);
        boolean repeatEnabled = getIntent().getBooleanExtra("REPEAT_ENABLED", false);

        // Tạo ObjectAnimator để xoay ImageView
        ImageView imageView = findViewById(R.id.imageView2);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(10000); // Thời gian xoay một vòng là 10000 milliseconds (5 giây)
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Xoay vô hạn lần
        animator.setInterpolator(new LinearInterpolator()); // Xoay đều không giật cục
        animator.start(); // Bắt đầu animation

        setupControls();

    }

    private void playMusic(String path) {
        // Phát nhạc từ đường dẫn
        try {
            mediaPlayer.reset();
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
                    String songTitle = songList.get(currentSongIndex).getTitle();
                    TextView titleTextView = findViewById(R.id.title);
                    if (titleTextView != null) {
                        titleTextView.setText(songTitle);}
                    titleTextView.setSelected(true);

                    // Cập nhật thời gian tổng cộng
                    int totalDuration = mediaPlayer.getDuration();
                    totalDurationView.setText(milliSecondsToTimer(totalDuration));
                    // Cập nhật thanh tiến độ
                    songProgressBar.setMax(totalDuration);
                    Log.d("Playlist_Songs"," songPath: " + path);
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Kiểm tra nếu không phải bài cuối cùng trong danh sách
                    if (currentSongIndex < songList.size() - 1) {
                        currentSongIndex++;
                        playMusic(songList.get(currentSongIndex).getPath());
                    } else {
                        // Nếu là bài cuối, quay lại bài đầu tiên hoặc dừng phát
                        currentSongIndex = 0;
                        playMusic(songList.get(currentSongIndex).getPath());
                    }
                }
            });
            Log.d("Playlist_Songs"," songPath: " + path);
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi ở đây
        }}

    private void setupControls() {
        ImageView playPauseButton = findViewById(R.id.play_pause_button);
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
                    playMusic(songList.get(currentSongIndex).getPath());
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
                    playMusic(songList.get(currentSongIndex).getPath());
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