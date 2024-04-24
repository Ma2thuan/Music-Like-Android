package com.example.googleoauthapp;

import static java.sql.Types.NULL;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googleoauthapp.Adapter.MusicApdater;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.Connectors.SongService;
import com.example.googleoauthapp.Connectors.VolleyCallBack;
import com.example.googleoauthapp.databinding.ActivityTop50Binding;

import java.util.ArrayList;

public class top50 extends AppCompatActivity {
    ActivityTop50Binding binding;
    ListView lvListMusic;

    MusicApdater musicApdater;

    SongService songService;
    ArrayList<Song> songs;

    ArrayList<Song> songArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityTop50Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        lvListMusic = findViewById(R.id.lvListMusic);
//        Log.d("SUCCESS", "GET IT");
//
//
//
        loadData();



//        EdgeToEdge.enable(this);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
//test
    private void loadData() {
        songs = new ArrayList<Song>();
        Log.i("test", "ok");

        songService.getRecentlyPlayedTracks(() -> {

                //songs = songService.getSongs();
                Log.d("TAG2" , String.valueOf(songs.size()));
        });







//        try {
//            songService.getRecentlyPlayedTracks(() -> {
//                try {
//                    //songs = songService.getSongs();
//                    Log.d("TAG2" , String.valueOf(songs.size()));
//                } catch (Exception e) {
////                    System.err.println("Error while getting songs: " + e.getMessage());
//                    Log.d("ERROR SONG" , e.getMessage());
//                }
//            });
//        } catch (Exception e) {
//            System.err.println("Error while getting recently played tracks: " + e.getMessage());
//            Log.d("ERROR RECENTLY" , e.getMessage());
//        }


//        Log.d("SUCCESS", "GET IT");
//        songArrayList = new ArrayList<>();
//        for (int i = 0 ; i < songs.size() ; i++){
//            songArrayList.add(new Song(songs.get(i).getId(),songs.get(i).getName(),R.drawable.imv3));
//        }
//
//        // Khởi tạo Adapter và gán dữ liệu cho ListView
//        musicApdater = new MusicApdater(top50.this, R.layout.item_list_music, songArrayList);
//
//        binding.lvListMusic.setAdapter(musicApdater);

        // Gọi hàm getRecentlyPlayedTracks để lấy danh sách bài hát mới nhất
//        songService.getRecentlyPlayedTracks(new VolleyCallBack() {
//            @Override
//            public void onSuccess() {
//                // Nạp dữ liệu từ ArrayList trả về từ getRecentlyPlayedTracks vào songs
//                ArrayList<Song> recentlyPlayedSongs = songService.getSongs();
//                if (recentlyPlayedSongs != null) {
//                    songs.addAll(recentlyPlayedSongs);
//                    // Cập nhật ListView sau khi đã nạp dữ liệu
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            musicApdater.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        });
    }

}