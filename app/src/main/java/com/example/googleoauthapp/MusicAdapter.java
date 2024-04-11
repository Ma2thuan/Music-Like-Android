package com.example.googleoauthapp;

import static com.example.googleoauthapp.GlobalVars.useremail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
    private ArrayList<HashMap<String, String>> songList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public MusicAdapter(ArrayList<HashMap<String, String>> songList) {
        this.songList = songList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_normal, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> song = songList.get(position);
        holder.songTitle.setText(song.get("songTitle"));

        // Thiết lập OnClickListener cho ImageView menu_button
        holder.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo PopupMenu tại đây
                PopupMenu popup = new PopupMenu(v.getContext(), holder.menuButton);
                popup.getMenuInflater().inflate(R.menu.popup_song, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        // Xử lý sự kiện khi một item menu được chọn

                        if (item.getItemId() == R.id.popup_song_add_to_playlist) {
                            // Hiển thị danh sách playlist hoặc tùy chọn tạo playlist mới
                            showPlaylistSelection(v.getContext(), song);}
                        return true;
                    }
                });
                popup.show(); // Hiển thị PopupMenu
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }
    private void playMusic(String path) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync(); // Chuẩn bị MediaPlayer một cách bất đồng bộ
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView menuButton;
        TextView songTitle;
        public ViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);

            ///
            menuButton = itemView.findViewById(R.id.menu_button);

            // Set the click listener for the itemView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the position of the clicked item
                    int position = getBindingAdapterPosition();
                    // Get the song path from the songList using the position
                    String songPath = songList.get(position).get("songPath");
                    Intent intent = new Intent(view.getContext(), screen_now_playing.class);
                    intent.putExtra("SONG_PATH", songPath);
                    intent.putExtra("SONG_LIST", songList);
                    intent.putExtra("SONG_INDEX", position);
                    view.getContext().startActivity(intent);
                }
            });
        }
        private void playMusic(String path) {
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void showPlaylistSelection(Context context, HashMap<String, String> song) {
        // Giả sử bạn có một phương thức để lấy danh sách các playlist
        ArrayList<String> playlists = getPlaylists();
        // Tạo và hiển thị AlertDialog với danh sách playlist
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn Playlist");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, playlists);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Thêm bài hát vào playlist được chọn
                addSongToPlaylist(playlists.get(which), song);
            }
        });
        builder.setPositiveButton("Tạo Playlist Mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Yêu cầu người dùng nhập tên playlist mới và thêm bài hát vào
                createNewPlaylistAndAddSong(context, song);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    // Phương thức giả định để thêm bài hát vào playlist
    private void addSongToPlaylist(String playlistName, HashMap<String, String> song) {
        // Cập nhật playlist với bài hát được chọn
    }

    // Phương thức giả định để lấy danh sách các playlist
    private ArrayList<String> getPlaylists() {
        // Lấy danh sách các playlist từ bộ nhớ hoặc cơ sở dữ liệu
        return new ArrayList<>();
    }

    // Phương thức giả định để tạo playlist mới và thêm bài hát
    private void createNewPlaylistAndAddSong(Context context, HashMap<String, String> song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Tạo Playlist Mới");

        // Thiết lập EditText để nhập tên playlist
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Thiết lập nút "Tạo"
        builder.setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String playlistName = input.getText().toString();
                if (!playlistName.isEmpty()) {
                    // Lưu playlist mới và thêm bài hát vào đây
                    saveNewPlaylist(playlistName, song);
                } else {
                    // Hiển thị thông báo lỗi nếu tên playlist trống
                    Toast.makeText(context, "Tên playlist không được để trống.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Thiết lập nút "Hủy"
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void saveNewPlaylist(String plName, HashMap<String, String> songs) {
        // Lưu playlist mới vào bộ nhớ hoặc cơ sở dữ liệu
        // Thêm bài hát vào playlist mới này
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = GlobalVars.getUserEmail();
        if (useremail != null) {

            // Kiểm tra xem đã có playlist nào với tên này chưa
            db.collection("users").document(useremail).collection("playlists")
                    .whereEqualTo("plName", plName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().isEmpty()) {
                            // Nếu không có tên trùng lặp, tạo playlist mới
                            // Tạo một HashMap cho playlist mới
                            Map<String, Object> playlist = new HashMap<>();
                            playlist.put("plName", plName);

                            // Thêm playlist mới vào Firestore với tên document là tên playlist
                            db.collection("users").document(userId).collection("playlists").document(plName).set(playlist)
                                    .addOnSuccessListener(aVoid -> {
                                        // Thêm bài hát vào playlist
                                        Map<String, Object> songData = new HashMap<>();
                                        songData.put("songID", songs.get("songID"));
                                        songData.put("songPath", songs.get("songPath"));
                                        songData.put("songTitle", songs.get("songTitle"));

                                        // Lấy tên bài hát từ songData
                                        String songTitle = (String) songData.get("songTitle");

                                        // Thêm bài hát vào sub-collection 'songs' của playlist
                                        db.collection("users").document(userId).collection("playlists").document(plName).collection("songs")
                                                .document(songTitle)
                                                .set(songData);
                                    })
                                    .addOnFailureListener(e -> {
                                        // Xử lý lỗi khi thêm playlist không thành công
                                    });
                        } else {
                            // Xử lý trường hợp tên playlist đã tồn tại
                        }
                    });
        } else {
            // Xử lý trường hợp không lấy được userId
        }
    }

   /* private void saveNewPlaylist(String plName, HashMap<String, String> songs) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userId = GlobalVars.getUserEmail();
        if (userId != null) {
            db.collection("users").document(userId).collection("playlists").document(plName).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (!document.exists()) {
                                // Playlist không tồn tại, tạo playlist mới và thêm bài hát
                                Map<String, Object> playlist = new HashMap<>();
                                playlist.put("plName", plName);
                                db.collection("users").document(userId).collection("playlists").document(plName).set(playlist)
                                        .addOnSuccessListener(aVoid -> {
                                            Map<String, Object> songData = new HashMap<>();
                                            songData.put("songID", songs.get("songID"));
                                            songData.put("songPath", songs.get("songPath"));
                                            songData.put("songTitle", songs.get("songTitle"));
                                            String songTitle = (String) songData.get("songTitle");
                                            db.collection("users").document(userId).collection("playlists").document(plName).collection("songs")
                                                    .document(songTitle)
                                                    .set(songData);
                                        });
                            } else {
                                // Playlist đã tồn tại, xử lý thêm bài hát vào playlist hiện có
                            }
                        } else {
                            // Xử lý lỗi
                        }
                    });
        }
    }
*/

}


