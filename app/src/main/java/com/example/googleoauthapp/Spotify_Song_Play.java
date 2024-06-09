package com.example.googleoauthapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.googleoauthapp.Connectors.SongService;
import com.example.googleoauthapp.databinding.ActivitySpotifySongPlayBinding;
import com.example.googleoauthapp.Class.Song;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.android.appremote.api.PlayerApi;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.PlayerState;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.spotify.android.appremote.api.PlayerApi;

import javax.security.auth.callback.Callback;

public class Spotify_Song_Play extends AppCompatActivity {


    ActivitySpotifySongPlayBinding binding;
    SongService service;
    ArrayList<Song> songs;
    private static final String CLIENT_ID ="4e5e333774674e6d9b9a34f9f68fe051";
    private static final String REDIRECT_URI = "http://localhost:8888/callback";

    private SpotifyAppRemote mSpotifyAppRemote;

    private Song song;

    private PlayerApi playerApi;
    int flag = 0;

    int timer = 0; /// TIMER

    int flagLikedSong = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpotifySongPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        service = new SongService(getApplicationContext());



        playCLickSong();
        loadData();
        event();

    }

    private void playCLickSong() {
        SpotifyAppRemote.connect(this, connectionParams,
                new Connector.ConnectionListener() {
                    @Override
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        mSpotifyAppRemote = spotifyAppRemote;
                        Log.d("SongPlay", "Connected! Yay!");

                        connected();

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
    }

    public void repeatTrack() {
        mSpotifyAppRemote.getPlayerApi().setRepeat(1);
    }

    private void connected() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        String message = sharedPreferences.getString("TRACK_ID_KEY", "");


        mSpotifyAppRemote.getPlayerApi().play("spotify:track:" + message);

        Log.d("SongPlay","spotify:track:"+message);
    }

    ConnectionParams connectionParams =
            new ConnectionParams.Builder(CLIENT_ID)
                    .setRedirectUri(REDIRECT_URI)
                    .showAuthView(true)
                    .build();




    private void event() {

        binding.songProgressbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser){
                    int timePosition = (int) (progress * getDuration() / 1000.0);
                    mSpotifyAppRemote.getPlayerApi().seekTo(timePosition);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        binding.repeatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.repeatButton.setSelected(!binding.repeatButton.isSelected());

                mSpotifyAppRemote.getPlayerApi().toggleRepeat();

                //service.repeatTrack();
            }
        });


        binding.playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (flag == 0)
                {
                    mSpotifyAppRemote.getPlayerApi().pause();
                    flag = 1;
                }
                else {
                    mSpotifyAppRemote.getPlayerApi().resume();
                    flag = 0;

                }

                binding.prevButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpotifyAppRemote.getPlayerApi().skipPrevious();
                        updateSong();
                    }
                });

                binding.nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSpotifyAppRemote.getPlayerApi().skipNext();
                        updateSong();
                    }
                });



                binding.likeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d("ERROR1" , "stuff");

                        Intent intent = getIntent();

                        songs = intent.<Song>getParcelableArrayListExtra("SongList");

                        int position = getIntent().getIntExtra("Order" , 0);



                        if (songs.size() > 0 && flagLikedSong == 1 ) {
                            service.removeSongToLibrary(songs.get(position));
                            flagLikedSong = 0;
                        }

                        else {
                            service.addSongToLibrary(songs.get(position));
                            flagLikedSong = 1;
                        }


                        //service.addSongToLibrary(songs.get(position));

                    }
                });


                binding.buttonRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        timer += 10;

                        Toast.makeText(Spotify_Song_Play.this, "Nhạc sẽ dừng sau " + timer , Toast.LENGTH_SHORT).show();

                        while (timer > 0){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            timer -= 1;
                        }

                        if (timer == 0 ){
                            mSpotifyAppRemote.getPlayerApi().pause();
                            Toast.makeText(Spotify_Song_Play.this, "Nhạc đã dừng" , Toast.LENGTH_SHORT).show();
                        }



                    }
                });







//                Intent intent = getIntent();
//
//                int position = getIntent().getIntExtra("Order", 0);
//
//                Log.d("ERROR", "onClick: " + "spotify:track:" + songs.get(position).getId());
//
//                JSONObject offsetJson = new JSONObject();
//                try {
//                    offsetJson.put("position", position);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                service.resumeSongRequest("spotify:track:" + songs.get(position).getId(), offsetJson);
            }
        });
    }

    private int getDuration()
    {
        return 200;
    }

    private void updateSong(){

        int position = getIntent().getIntExtra("Order" , 0);

        binding.songName.setText((songs.get(position).getName()));
    }

    private void loadData() {
        songs = new ArrayList<Song>();

        Intent intent = getIntent();

        songs = intent.<Song>getParcelableArrayListExtra("SongList");
        int position = getIntent().getIntExtra("Order" , 0);
        binding.songName.setText((songs.get(position).getName()));



        //Play Song
        if (songs.size() > 0) {

            String trackID = songs.get(position).getId();
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putString("TRACK_ID_KEY", trackID);
            editor.apply();

        }
    }


}