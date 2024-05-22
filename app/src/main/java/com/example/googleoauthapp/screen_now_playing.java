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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.example.googleoauthapp.ui.dashboard.DashboardViewModel;
import com.google.firebase.FirebaseApp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class screen_now_playing extends AppCompatActivity {
    MediaPlayer mediaPlayer = new MediaPlayer();// Tạo instance của MediaPlayer
    private ArrayList<HashMap<String, String>> songList;
    private int currentSongIndex = 0;//Biến theo dõi vị trí bài hát hiện tại
    boolean isPlaying = false; // Biến theo dõi trạng thái phát nhạc
    private SeekBar songProgressBar; //Biến theo dõi thanh progress bar
    private TextView currentDurationView;//Biến theo dõi thời gian hiện tại
    private TextView totalDurationView;// Biến theo dõi thời gian của bài hát


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

        // Tìm ImageView bằng ID
        ImageView imageView = findViewById(R.id.imageView2);

        // Tạo ObjectAnimator để xoay ImageView
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(10000); // Thời gian xoay một vòng là 10000 milliseconds (10 giây)
        animator.setRepeatCount(ObjectAnimator.INFINITE); // Xoay vô hạn lần
        animator.setInterpolator(new LinearInterpolator()); // Xoay đều
        animator.start(); // Bắt đầu animation

        // Thiết lập các nút điều khiển
        setupControls();
    }
    private void setupControls() {

        //Sự kiện dừng/phát nhạc
        ImageView playPauseButton = findViewById(R.id.play_pause_button);
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra trạng thái và thay đổi hình ảnh và trạng thái phát nhạc
                if (isPlaying) {
                    mediaPlayer.pause(); // Dừng phát nhạc
                    // Thay đổi hình ảnh thành icon play
                    playPauseButton.setImageResource(R.drawable.ic_play_white);
                    isPlaying = false;
                } else {
                    mediaPlayer.start(); // Bắt đầu phát nhạc
                    // Thay đổi hình ảnh thành icon pause
                    playPauseButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    isPlaying = true;
                }
            }
        });


        // Sự kiện chuyển sang bài hát phía trước trong list nhạc
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

        //Sự kiện chuển sang bài hát phía sau trong list nhạc
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
        //Sự kiện chuyển trở về màn hình phía trước
        ImageView backButton = findViewById(R.id.button_right);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  /*              Intent intent = new Intent(view.getContext(), DashboardViewModel.class);
                view.getContext().startActivity(intent);
                finish();*/
            }
        });
        //ProgressBar
        songProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    currentDurationView.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
                }
            }

            //Hàm bắt đầu sự kiện kéo/thay đổi thanh tiến độ(progress bar)
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }
            //Hàm kết thúc sự kiện kéo/thay đổi thanh tiến độ(progress bar) và thay đổi các giá trị
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

                    // Cập nhật thời gian của bài hát
                    int totalDuration = mediaPlayer.getDuration();
                    totalDurationView.setText(milliSecondsToTimer(totalDuration));
                    // Cập nhật thanh tiến độ
                    songProgressBar.setMax(totalDuration);
                    Log.d("Playlist_Songs"," songPath: " + path);
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
                        // Nếu là bài cuối, quay lại bài đầu tiên
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

    //Hàm chuyển đổi thời gian bài hát
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
    // Giải phóng MediaPlayer khi không cần thiết
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
