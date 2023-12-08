package com.example.personalfinanceapp.model;

public class UserContext {
    private static String username;
    private static String firstName;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserContext.username = username;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        UserContext.firstName = firstName;
    }
}

