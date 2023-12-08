package com.example.personalfinanceapp.model;

import javafx.scene.control.Button;

public class Goal {
    private final String Id;
    private final String month;
    private final int expected;
    private final int actual;
    private final boolean done;
    private Button edit;
    public boolean isDone() {
        return done;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }




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


    public String getMonth() {
        return month;
    }


    public int getExpected() {
        return expected;
    }


    public int getActual() {
        return actual;
    }


}