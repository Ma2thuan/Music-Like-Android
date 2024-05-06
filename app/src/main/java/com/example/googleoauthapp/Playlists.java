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

    private void loadPlaylistsFromFirestore() {
        //Tạo instance FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Lấy userEmail từ GlobalVars
        String userEmail = GlobalVars.getUserEmail();
        if (userEmail != null) {//Truy vấn và load dữ liệu từ Firestore
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