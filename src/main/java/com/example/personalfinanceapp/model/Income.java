package com.example.personalfinanceapp.model;

import javafx.scene.control.Button;

import java.util.Date;

public class Income {
    private String Id;
    private String title;
    private String category;
    private String description;
    private int amount;
    private Date date;
    private Button edit;
    private Button delete;

    public Income(String id, String title, String category, String description, int amount, Date date) {
        Id = id;
        this.title = title;
        this.category = category;
        this.description = description;
        this.amount = amount;
        this.date = date;
    }



    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }
}


