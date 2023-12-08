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

    public Button loginButton;
    @FXML
    private Button exitButton;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField usernameTextfield;
    @FXML
    private Label labelUsername;
    @FXML
    void loginButtonOnAction() {
        Alert alert;
        if (!usernameTextfield.getText().isBlank() && !passwordPasswordField.getText().isBlank()){
            validateLogin();
        } else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Алдаа");
            alert.setHeaderText(null);
            alert.setContentText("Дутуу талбаруудыг нөхнө үү.");
            alert.showAndWait();
        }
    }

    @FXML
    void exitButtonOnAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
    public void validateLogin() {
        Connection connectDB = DatabaseConnection.getConnection();

        String verifyLogin = "SELECT count(1) FROM login_info WHERE username = '"+usernameTextfield.getText()+"' AND password = '"+passwordPasswordField.getText()+"'";

        Alert alert;

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    UserContext.setUsername(usernameTextfield.getText());
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Мэдэгдэл");
                    alert.setHeaderText(null);
                    alert.setContentText("Амжилттай нэвтэрлээ.");
                    alert.showAndWait();
                    Stage stage = (Stage) exitButton.getScene().getWindow();
                    stage.close();

                    gotoUserStage();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Алдаа");
                    alert.setHeaderText(null);
                    alert.setContentText("Хэрэглэгчийн нэр/Нууц үг/Нэвтрэх эрх буруу байна.");
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
