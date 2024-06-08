package com.example.googleoauthapp;

public class GlobalVars {
    private static String userId;

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String uid) {
        userId = uid;
    }

    public static String useremail;

    public static String getUserEmail() {
        return useremail;
    }

    public static void setUserEmail(String email) {
        useremail = email;
    }

    private static String token;
    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        GlobalVars.token = token;
    }
}
