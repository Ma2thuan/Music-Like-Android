package com.example.googleoauthapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googleoauthapp.model.Playlist;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {

    private List<Playlist> playlists;

    public PlaylistAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }
    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist_child, parent, false);
        return new PlaylistViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        Playlist playlist = playlists.get(position);
        holder.titleTextView.setText(playlist.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Playlist_Songs.class);
                String playlistName = playlists.get(holder.getAdapterPosition()).getName(); // Giả sử Playlist có phương thức getId()
                intent.putExtra("PLAYLIST_NAME", playlistName);
                v.getContext().startActivity(intent);
            }
        });

        // Sự kiện nhấn giữ để xóa playlist
        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có muốn xóa playlist này không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        deletePlaylist(playlist.getName(), v.getContext());
                    })
                    .setNegativeButton("Không", null)
                    .show();
            return true;
        });
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }
    public static class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        public PlaylistViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    // Phương thức để xóa playlist từ Firestore
    private void deletePlaylist(String playlistId, Context context) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String userEmail = GlobalVars.getUserEmail();
        if (userEmail != null) {
            db.collection("users").document(userEmail).collection("playlists").document(playlistId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Xóa thành công, cập nhật UI ở đây
                        Toast.makeText(context, "Playlist đã được xóa.", Toast.LENGTH_SHORT).show();
                        // Cập nhật lại danh sách playlist
                        playlists.removeIf(playlist -> playlist.getName().equals(playlistId));
                        notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        // Xử lý lỗi
                        Toast.makeText(context, "Lỗi khi xóa playlist.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(context, "Email người dùng không tồn tại.", Toast.LENGTH_SHORT).show();
        }
    }
}
