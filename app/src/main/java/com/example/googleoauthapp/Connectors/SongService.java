package com.example.googleoauthapp.Connectors;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.googleoauthapp.Class.Song;
import com.example.googleoauthapp.Class.SongTest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SongService {
    private ArrayList<Song> songs = new ArrayList<>();

    //Test
    private ArrayList<SongTest> songTests = new ArrayList<>();

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    public SongService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }



    public ArrayList<Song> getRecentlyPlayedTracks(final VolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }


    public ArrayList<Song> getTop50(final VolleyCallBack callBack) {

        String endpoint = "https://api.spotify.com/v1/playlists/37i9dQZEVXbMDoHDwVN2tF";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();

                    // Clear existing songs
                    songs.clear();

                    // Navigate JSON to retrieve tracks
                    JSONArray tracksArray = response.optJSONObject("tracks").optJSONArray("items");
                    if (tracksArray != null) {
                        for (int i = 0; i < tracksArray.length(); i++) {
                            JSONObject trackObject = tracksArray.optJSONObject(i);
                            if (trackObject != null) {
                                JSONObject trackInfo = trackObject.optJSONObject("track");
                                if (trackInfo != null) {
                                    Song song = gson.fromJson(trackInfo.toString(), Song.class);
                                    songs.add(song);
                                }
                            }
                        }
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "getTop50: ");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return songs;
    }

    public ArrayList<Song> getTop50VN(final VolleyCallBack callBack) {

        String endpoint = "https://api.spotify.com/v1/playlists/37i9dQZEVXbLdGSmz6xilI";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();

                    // Clear existing songs
                    songs.clear();

                    // Navigate JSON to retrieve tracks
                    JSONArray tracksArray = response.optJSONObject("tracks").optJSONArray("items");
                    if (tracksArray != null) {
                        for (int i = 0; i < tracksArray.length(); i++) {
                            JSONObject trackObject = tracksArray.optJSONObject(i);
                            if (trackObject != null) {
                                JSONObject trackInfo = trackObject.optJSONObject("track");
                                if (trackInfo != null) {
                                    Song song = gson.fromJson(trackInfo.toString(), Song.class);
                                    songs.add(song);
                                }
                            }
                        }
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "getTop50: ");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return songs;
    }

    public ArrayList<Song> getTopNgotBand(final VolleyCallBack callBack) {

        String endpoint = "https://api.spotify.com/v1/playlists/37i9dQZF1EIXuzAJlE00Hk";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();

                    // Clear existing songs
                    songs.clear();

                    // Navigate JSON to retrieve tracks
                    JSONArray tracksArray = response.optJSONObject("tracks").optJSONArray("items");
                    if (tracksArray != null) {
                        for (int i = 0; i < tracksArray.length(); i++) {
                            JSONObject trackObject = tracksArray.optJSONObject(i);
                            if (trackObject != null) {
                                JSONObject trackInfo = trackObject.optJSONObject("track");
                                if (trackInfo != null) {
                                    Song song = gson.fromJson(trackInfo.toString(), Song.class);
                                    songs.add(song);
                                }
                            }
                        }
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "getTop50: ");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return songs;
    }

    public ArrayList<Song> getTheStrokes(final VolleyCallBack callBack) {

        String endpoint = "https://api.spotify.com/v1/playlists/37i9dQZF1DZ06evO04caZO";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest

                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();

                    // Clear existing songs
                    songs.clear();

                    // Navigate JSON to retrieve tracks
                    JSONArray tracksArray = response.optJSONObject("tracks").optJSONArray("items");
                    if (tracksArray != null) {
                        for (int i = 0; i < tracksArray.length(); i++) {
                            JSONObject trackObject = tracksArray.optJSONObject(i);
                            if (trackObject != null) {
                                JSONObject trackInfo = trackObject.optJSONObject("track");
                                if (trackInfo != null) {
                                    Song song = gson.fromJson(trackInfo.toString(), Song.class);
                                    songs.add(song);
                                    Log.d("hello2", String.valueOf(songs.get(0).getPhoto()));
                                }
                            }
                        }
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                    Log.d("ERROR", "getTop50: ");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
        return songs;
    }

    public void addSongToLibrary(Song song) {
        JSONObject payload = preparePutPayload(song);
        JsonObjectRequest jsonObjectRequest = prepareSongLibraryRequest(payload);
        queue.add(jsonObjectRequest);
    }

    private JsonObjectRequest prepareSongLibraryRequest(JSONObject payload) {
        return new JsonObjectRequest(Request.Method.PUT, "https://api.spotify.com/v1/me/tracks", payload, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
    }

    public void skipToNextTrack() {
        String endpoint = "https://api.spotify.com/v1/me/player/next";
        Log.d("ERROR", "skipToNextTrack: " );
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, endpoint, null,
                response -> {
                    // Handle successful response here, if needed
                    Log.d("ERROR", "skipToNextTrack: " + response.toString());
                },
                error -> {
                    // Handle error here
                    Log.d("ERROR", "skipToNextTrack: " + error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public void repeatTrack() {

            String endpoint = "https://api.spotify.com/v1/me/player/repeat";
            Log.d("ERROR", "repeatTrack: ");

            String url = endpoint + "?state=track";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.PUT, url, null,
                    response -> {
                        // Handle successful response here, if needed
                        Log.d("ERROR", "repeatTrack: " + response.toString());
                    },
                    error -> {
                        // Handle error here
                        Log.d("ERROR", "repeatTrack: " + error.toString());
                        Log.d("ERROR", "repeatTrack: " + error.getMessage());

                        // Get the error code
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse!= null) {
                            int errorCode = networkResponse.statusCode;
                            Log.d("ERROR", "Error Code: " + errorCode);
                        } else {
                            Log.d("ERROR", "Error Code: Unknown");
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String token = sharedPreferences.getString("token", "");
                    String auth = "Bearer " + token;
                    headers.put("Authorization", auth);
                    return headers;
                }
            };
            queue.add(jsonObjectRequest);
        }


    private String getRepeatState() {
        // Make a GET request to the repeat endpoint to get the current repeat state
        String endpoint = "https://api.spotify.com/v1/me/player/repeat";
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET, endpoint,
                response -> {
                    // Extract the repeat state from the response
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        String state = responseJson.getString("state");
                        // Do something with the repeat state
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error here
                    Log.d("ERROR", "getRepeatState: " + error.toString());
                    Log.d("ERROR", "getRepeatState: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };

        queue.add(stringRequest);

        return endpoint;
    }



    public void resumeSongRequest(String contextUri, JSONObject offsetPosition) {
        String endpoint = "https://api.spotify.com/v1/me/player/play";
        Log.d("ERROR", "startOrResumeTrackWithContext: ");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("context_uri", contextUri);
            if (offsetPosition!= null) {
                int position = offsetPosition.getInt("position");
                JSONObject offsetJson = new JSONObject();
                offsetJson.put("position", position);
                jsonObject.put("offset", offsetJson);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT, endpoint, jsonObject,
                response -> {
                    // Handle successful response here, if needed
                    Log.d("ERROR", "startOrResumeTrackWithContext: " + response.toString());
                },
                error -> {
                    // Handle error here
                    Log.d("ERROR", "startOrResumeTrackWithContext: " + error.toString());
                    Log.d("ERROR", "startOrResumeTrackWithContext: " + error.getMessage());

                    // Get the error code
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse!= null) {
                        int errorCode = networkResponse.statusCode;
                        Log.d("ERROR", "Error Code: " + errorCode);
                    } else {
                        Log.d("ERROR", "Error Code: Unknown");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private JSONObject preparePutPayload(Song song) {
        JSONArray idarray = new JSONArray();
        idarray.put(song.getId());
        JSONObject ids = new JSONObject();
        try {
            ids.put("ids", idarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public ArrayList<Song> getSongs() {
        Log.d("SIZE" , String.valueOf(songs.size()));
        return songs;
    }

    public ArrayList<SongTest> getSongsTest() {
        Log.d("SIZE" , String.valueOf(songs.size()));
        return songTests;
    }




}
