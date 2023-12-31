package com.example.personalfinanceapp.controller;

import com.example.personalfinanceapp.db.DatabaseConnection;
import com.example.personalfinanceapp.model.UserContext;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;

public class LoginController {

    @FXML
    private Button exitButton;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextField tfUsername;
    @FXML
    private Label labelUsername;
    @FXML
    void login() {
        Alert alert;
        if (!tfUsername.getText().isBlank() && !pfPassword.getText().isBlank()){
            validateLogin();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Fill in the missing fields.");
            alert.showAndWait();
        }
    }

    @FXML
    void exitProgram() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void validateLogin() {
        Connection connectDB = DatabaseConnection.getConnection();

        String verifyLogin = "SELECT count(1) FROM login_info WHERE username = '"+tfUsername.getText()+"' AND password = '"+pfPassword.getText()+"'";

        Alert alert;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    UserContext.setUsername(tfUsername.getText());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login successful.");
                    alert.showAndWait();
                    Stage stage = (Stage) exitButton.getScene().getWindow();
                    stage.close();

                    gotoUserStage();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Incorrect username or password.Please try again.");
                    alert.showAndWait();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  gotoUserStage() throws IOException {
        Parent userStageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/personalfinanceapp/userDashboard.fxml")));
        Stage userStage = new Stage();
        Scene userStageScene = new Scene(userStageParent);
        userStage.setScene(userStageScene);
        userStage.setTitle("Personal Finance");
        userStage.setResizable(false);
        userStage.show();
    }
}
