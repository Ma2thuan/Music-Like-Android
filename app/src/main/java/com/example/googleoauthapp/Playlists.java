package com.example.googleoauthapp;

import static android.content.ContentValues.TAG;

import static com.example.googleoauthapp.GlobalVars.useremail;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.googleoauthapp.model.Playlist;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.checkerframework.checker.units.qual.A;

public class Playlists extends AppCompatActivity {
   /* FirebaseFirestore db = FirebaseFirestore.getInstance();*/
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private List<Playlist> playlistList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_playlists);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Sử dụng 2 cột trong GridLayoutManager
        adapter = new PlaylistAdapter(playlistList);
        recyclerView.setAdapter(adapter);

        loadPlaylistsFromFirestore();}


/*        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String useremail = GlobalVars.getUserEmail(); // Đảm bảo rằng biến này được khai báo và không null

        if (useremail != null) {
            // Truy vấn đến collection 'playlists'
            db.collection("users").document(useremail).collection("playlists")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot playlistDocument : task.getResult()) {
                                    String playlistId = playlistDocument.getId();
                                    Log.d(TAG, "Playlist ID: " + playlistId);

                                    // Truy vấn đến collection 'songs' trong mỗi 'playlist'
                                    db.collection("users").document(useremail).collection("playlists").document(playlistId).collection("songs")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot songDocument : task.getResult()) {
                                                            String songName = songDocument.getId(); // Tên của document là tên bài hát
                                                            Log.d(TAG, "Song name: " + songName);
                                                        }
                                                    } else {
                                                        Log.w(TAG, "Error getting songs.", task.getException());
                                                    }
                                                }
                                            });
                                }
                            } else {
                                Log.w(TAG, "Error getting playlists.", task.getException());
                            }
                        }
                    });
        } else {
            // Xử lý trường hợp không lấy được useremail
            Log.w(TAG, "User email is null");
        }*/




/*        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = GlobalVars.getUserEmail();
        if (useremail != null) {
            db.collection("users").document(useremail).collection("playlists")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                ArrayList<String> playlistNames = new ArrayList<>();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    playlistNames.add(document.getId());
                                }
                                // Cập nhật adapter với danh sách playlist mới
                                *//*musicAdapter.updatePlaylists(playlistNames);*//*
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        } else {
            // Xử lý trường hợp không lấy được useremail
            Log.w(TAG, "User email is null");
        }
    }*/
    private void loadPlaylistsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = GlobalVars.getUserEmail();
        if (userEmail != null) {
            db.collection("users").document(userEmail).collection("playlists")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Playlist playlist = new Playlist(document.getId());
                                playlistList.add(playlist);
                            }
                            adapter.notifyDataSetChanged(); // Thông báo cho adapter biết dữ liệu đã thay đổi
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    });
        } else {
            Log.w(TAG, "User email is null");
        }
    }
}