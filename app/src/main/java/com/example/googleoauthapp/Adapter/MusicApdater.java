package com.example.googleoauthapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.R;


import java.util.ArrayList;

public class MusicApdater extends BaseAdapter {
    Activity activity;
    int item_layout;
    ArrayList<Song> songs;

    public MusicApdater(Activity activity, int item_layout, ArrayList<Song> songs) {
        this.activity = activity;
        this.item_layout = item_layout;
        this.songs = songs;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public int getItem_layout() {
        return item_layout;
    }

    public void setItem_layout(int item_layout) {
        this.item_layout = item_layout;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int position) {
        return songs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView ==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(item_layout,null);
            holder.imvPhoto = convertView.findViewById(R.id.imvPhoto);
            holder.txtMusicName = convertView.findViewById(R.id.txtMusicName);
            holder.txtId = convertView.findViewById(R.id.txtId);

            convertView.setTag(holder);


        }else{
            holder = (ViewHolder) convertView.getTag();
            //Binding data

        }
        Song song = songs.get(position);
        holder.imvPhoto.setImageResource(song.getPhoto());
        holder.txtMusicName.setText(song.getName());
        holder.txtId.setText(song.getId());
        return convertView;
    }

    public class ViewHolder {
        ImageView imvPhoto;
        TextView txtMusicName,txtId;

        public ImageView getImvPhoto() {
            return imvPhoto;
        }

        public void setImvPhoto(ImageView imvPhoto) {
            this.imvPhoto = imvPhoto;
        }

        public TextView getTxtMusicName() {
            return txtMusicName;
        }

        public void setTxtMusicName(TextView txtMusicName) {
            this.txtMusicName = txtMusicName;
        }

        public TextView getTxtId() {
            return txtId;
        }

        public void setTxtId(TextView txtId) {
            this.txtId = txtId;
        }
    }
}
