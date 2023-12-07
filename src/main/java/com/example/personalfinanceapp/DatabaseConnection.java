package com.example.personalfinanceapp;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public Connection databaseLink;

    public static Connection getConnection() {
        DatabaseConnection instance = new DatabaseConnection();
        instance.initializeConnection();
        return instance.databaseLink;
    }

    private void initializeConnection() {
        String databaseName = "personal_finance_database";
        String databaseUser = "root";
        String databasePassword = "password";
        String url = "jdbc:mysql://localhost/" + databaseName;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
