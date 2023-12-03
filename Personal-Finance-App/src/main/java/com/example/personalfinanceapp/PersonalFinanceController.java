package com.example.personalfinanceapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PersonalFinanceController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}