package com.example.personalfinanceapp.model;

import javafx.scene.control.Button;

public class Goal {
    private String Id;
    private String month;
    private int expected;
    private int actual;
    private boolean done;
    private Button edit;


    public Goal(String id, String month, int expected, int actual, boolean done) {
        Id = id;
        this.month = month;
        this.expected = expected;
        this.actual = actual;
        this.done = done;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    public int getActual() {
        return actual;
    }

    public void setActual(int actual) {
        this.actual = actual;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}