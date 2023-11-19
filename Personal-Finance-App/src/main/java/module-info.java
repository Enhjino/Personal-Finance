module com.example.personalfinanceapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.personalfinanceapp to javafx.fxml;
    exports com.example.personalfinanceapp;
}