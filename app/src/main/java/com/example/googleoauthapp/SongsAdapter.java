package com.example.googleoauthapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Song;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {
    String useremail = GlobalVars.getUserEmail();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Song> songList;
    private SongItemClickListener songItemClickListener;
    private String playlistName; // Trường mới để lưu trữ tên playlist



    public SongsAdapter(List<Song> songs, SongItemClickListener listener, String playlistName) {
        this.songList = new ArrayList<>(songs);
        this.songItemClickListener = listener;
        this.playlistName = playlistName; // Lưu trữ tên playlist
    }
    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_song, parent, false);
        return new SongViewHolder(view);
    }
@Override
public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
    Song song = songList.get(position);
    holder.songTitleTextView.setText(song.getTitle());
    holder.itemView.setOnClickListener(view -> {
        if (songItemClickListener != null) {
            songItemClickListener.onSongClick(songList, position); // Truyền cả songList và vị trí
        }
    });

    holder.menuButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Khởi tạo PopupMenu tại đây
            PopupMenu popup = new PopupMenu(v.getContext(), holder.menuButton);
            popup.getMenuInflater().inflate(R.menu.popup_song_playlist, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    // Xử lý sự kiện khi một item menu được chọn
                    if (item.getItemId() == R.id.popup_song_remove_playlist) {
                        // Lấy bài hát được chọn
                        Song song = songList.get(holder.getAdapterPosition());

                        // Xóa bài hát khỏi playlist trong Firestore
                        removeSongFromPlaylist(song.getTitle(), playlistName);

                        // Cập nhật danh sách bài hát cục bộ và thông báo cho adapter
                        songList.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }

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
    public class SongViewHolder extends RecyclerView.ViewHolder {
        ImageView menuButton;
        TextView songTitleTextView;

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitleTextView = itemView.findViewById(R.id.title);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }

public interface SongItemClickListener {
    void onSongClick(ArrayList<Song> songList, int position); // Cập nhật interface để nhận cả ArrayList và vị trí
}
public void setSongs(List<Song> songs) {
    this.songList = new ArrayList<>(songs);
    notifyDataSetChanged();
}
    private void showPlaylistSelection(Context context, HashMap<String, String> song) {

    }

    private void removeSongFromPlaylist(String songTitle, String playlistName) {
        String userEmail = GlobalVars.getUserEmail();
        if (userEmail != null && playlistName != null && songTitle != null) {
            db.collection("users").document(userEmail).collection("playlists")
                    .document(playlistName).collection("songs")
                    .document(songTitle)
                    .delete()
                    .addOnSuccessListener(aVoid -> Log.d("SongsAdapter", "Bài hát đã được xóa khỏi playlist."))
                    .addOnFailureListener(e -> Log.w("SongsAdapter", "Lỗi khi xóa bài hát.", e));
        }
    }

}
