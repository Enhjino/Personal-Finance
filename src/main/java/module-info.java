module com.example.personalfinanceapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.example.personalfinanceapp.model to javafx.base;
    opens com.example.personalfinanceapp to javafx.fxml;
    exports com.example.personalfinanceapp;
}