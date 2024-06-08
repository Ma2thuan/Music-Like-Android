package com.example.googleoauthapp.model;

public class Song_Dri {
    private String name;
    private String url;

    public Song_Dri(String name, String url) {
        this.name = name;
        this.url = url;
    }

    // Getters và Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

