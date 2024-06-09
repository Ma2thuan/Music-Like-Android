package com.example.googleoauthapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class MusicPlayerService extends Service {
    // ... các phương thức khác

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if ("STOP_MUSIC".equals(intent.getAction())) {
            // Dừng phát nhạc từ Spotify
            // Đóng ứng dụng hoặc hoạt động cần thiết
            stopSelf();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

