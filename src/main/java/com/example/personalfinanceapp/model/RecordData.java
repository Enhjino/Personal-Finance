package com.example.personalfinanceapp.model;

public interface RecordData {
    String getId();

    default String getTitle() {
        return "Default Title";
    }
}
