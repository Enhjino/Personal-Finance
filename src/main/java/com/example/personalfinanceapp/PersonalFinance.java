package com.example.personalfinanceapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PersonalFinance extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PersonalFinance.class.getResource("/com/example/personalfinanceapp/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Personal Finance App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
