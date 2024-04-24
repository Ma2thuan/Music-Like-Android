package com.example.googleoauthapp.model;

import java.io.Serializable;

public class Song implements Serializable {
    private String title;
    public String path; // Giả sử bạn cũng muốn lưu trữ đường dẫn của bài hát

    public Song(String title, String path) {

        this.title = title;
        this.path = path;
    }

    // Getters và Setters
    public String getTitle() {
        return title;
    }


    public String getPath() {
        return path;
    }
}
