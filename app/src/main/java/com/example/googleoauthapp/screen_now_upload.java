package com.example.googleoauthapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
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
import androidx.media3.common.Player;


import com.example.googleoauthapp.ui.dashboard.DashboardViewModel;

@UnstableApi public class screen_now_upload extends AppCompatActivity {
    private SimpleExoPlayer player;
    private TextView songTitleTextView;
    private String userAgent = "GoogleOAuthApp/1.0 (Android)";
    boolean isPlaying = false;
    private SeekBar songProgressBar;
    private TextView currentDurationView;
    private TextView totalDurationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_now_upload);

        player = new SimpleExoPlayer.Builder(this).build();
        songTitleTextView = findViewById(R.id.title);


        songProgressBar = findViewById(R.id.song_progressbar);
        currentDurationView = findViewById(R.id.song_current_duration);

        updateProgressBar();

        mHandler.postDelayed(mUpdateTimeTask, 1000);

        totalDurationView = findViewById(R.id.song_total_duration);
        int totalDuration = (int) player.getDuration();
        totalDurationView.setText(milliSecondsToTimer(totalDuration));
        // Cập nhật thanh tiến độ
        songProgressBar.setMax((int) totalDuration);

        String songUrl = getIntent().getStringExtra("SONG_URL");
        String songName = getIntent().getStringExtra("SONG_NAME");

        if (songTitleTextView != null) {
            songTitleTextView.setText(songName);
        }

        // Tạo ObjectAnimator để xoay ImageView
        ImageView imageView = findViewById(R.id.imageView2);
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(10000); // Thời gian xoay một vòng là 10000 milliseconds (5 giây)
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Xoay vô hạn lần
        animator.setInterpolator(new LinearInterpolator()); // Xoay đều không giật cục
        animator.start(); // Bắt đầu animation

        setupControls();

        MediaSource mediaSource = buildMediaSource(Uri.parse(songUrl));
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);



        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_READY && !player.isCurrentWindowDynamic()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int totalDuration = (int) player.getDuration();
                            totalDurationView.setText(milliSecondsToTimer(totalDuration));
                            songProgressBar.setMax(totalDuration);
                        }
                    });
                }
            }
        });




    }

    private MediaSource buildMediaSource(Uri uri) {
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory().setUserAgent(userAgent);
        MediaItem mediaItem = MediaItem.fromUri(uri);
        return new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem);
    }



    private void setupControls() {
        ImageView playPauseButton = findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái và thay đổi hình ảnh và trạng thái phát nhạc
                if (isPlaying) {
                    player.pause(); // Dừng phát nhạc
                    playPauseButton.setImageResource(R.drawable.ic_play_white); // Thay đổi hình ảnh thành icon play
                    isPlaying = false;
                } else {
                    player.play(); // Bắt đầu phát nhạc
                    playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp); // Thay đổi hình ảnh thành icon pause
                    isPlaying = true;
                }
            }
        });

/*        ImageView prevButton = findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang bài hát phía trước
                if (currentSongIndex > 0) {
                    currentSongIndex--;
                    playMusic(songList.get(currentSongIndex).getPath());
                }
            }
        });*/

/*        ImageView nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang phát bài hát tiếp theo
                if (currentSongIndex < songList.size() - 1) {
                    currentSongIndex++;
                    playMusic(songList.get(currentSongIndex).getPath());
                }
            }
        });*/
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
                    player.seekTo(progress);
                    /*mediaPlayer.seekTo(progress);*/
                    currentDurationView.setText(milliSecondsToTimer(player.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = (int) player.getDuration();
                int currentPosition = seekBar.getProgress();
                player.seekTo(currentPosition);
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
            if (player != null) {
                long currentDuration = player.getCurrentPosition();
                currentDurationView.setText(milliSecondsToTimer(currentDuration));
                songProgressBar.setProgress((int)currentDuration);
            }
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