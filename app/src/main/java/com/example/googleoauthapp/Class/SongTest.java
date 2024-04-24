package com.example.googleoauthapp.Class;

public class SongTest {
    private String id;
    private String name;

    public String photo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public SongTest(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }
}
