package com.example.googleoauthapp.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.googleoauthapp.MusicAdapter;
import com.example.googleoauthapp.Playlists;
import com.example.googleoauthapp.R;
import com.example.googleoauthapp.databinding.FragmentDashboardBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private ArrayList<HashMap<String, String>> songList;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 1;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // Lấy danh sách bài hát từ thiết bị
        songList = getMusicFiles();
        musicAdapter = new MusicAdapter(songList);
        recyclerView.setAdapter(musicAdapter);

        Button playlistButton = view.findViewById(R.id.button_playlist);
        playlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tạo Intent để khởi động Activity Playlists
                Intent intent = new Intent(getActivity(), Playlists.class);
                startActivity(intent);
            }
        });

    }



    public ArrayList<HashMap<String, String>> getMusicFiles() {
        ArrayList<HashMap<String, String>> fileList = new ArrayList<>();
        // Uri for external music files
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        // Projection for the columns we want to retrieve
        String[] projection = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA
        };
        // Query the content resolver
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                HashMap<String, String> song = new HashMap<>();
                song.put("songTitle", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                song.put("songPath", cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                // Adding each song to the list
                fileList.add(song);
            }
            cursor.close();
        }
        return fileList;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}