package com.example.googleoauthapp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MusicShutdownReceiver extends BroadcastReceiver {
@Override
public void onReceive(Context context, Intent intent) {
    // Tạo Intent để đóng tất cả các Activity
    Intent closeIntent = new Intent(context, SplashActivity.class); // Sử dụng MainActivity hoặc Activity khởi đầu của bạn
    closeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    closeIntent.putExtra("EXIT", true); // Bạn có thể sử dụng một Extra để kiểm tra trong MainActivity
    context.startActivity(closeIntent);
}
}


