package com.example.personalfinanceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Optional;

public class UserDashboardController {

    @FXML
    private BarChart<?, ?> barChardIncome;

    @FXML
    private BarChart<?, ?> barChartExpense;

    @FXML
    private Button btnBudget;

    @FXML
    private Button btnDashboard;

    @FXML
    private Button btnExpense;

    @FXML
    private Button btnGoals;

    @FXML
    private Button btnIncome;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnReport;

    @FXML
    private ChoiceBox<?> choiceBoxDate;

    @FXML
    private TableColumn<?, ?> colExpenseAmount;

    @FXML
    private TableColumn<?, ?> colExpenseCategory;

    @FXML
    private TableColumn<?, ?> colExpenseDate;

    @FXML
    private TableColumn<?, ?> colExpenseDelete;

    @FXML
    private TableColumn<?, ?> colExpenseDescription;

    @FXML
    private TableColumn<?, ?> colExpenseID;

    @FXML
    private TableColumn<?, ?> colExpenseName;

    @FXML
    private TableColumn<?, ?> colIncomeAmount;

    @FXML
    private TableColumn<?, ?> colIncomeCategory;

    @FXML
    private TableColumn<?, ?> colIncomeDate;

    @FXML
    private TableColumn<?, ?> colIncomeDelete;

    @FXML
    private TableColumn<?, ?> colIncomeDescription;

    @FXML
    private TableColumn<?, ?> colIncomeID;

    @FXML
    private TableColumn<?, ?> colIncomeTitle;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private DatePicker datePicker;

    @FXML
    private AnchorPane expensePane;

    @FXML
    private AnchorPane incomePane;

    @FXML
    private Label labelExpense;

    @FXML
    private Label labelIncome;

    @FXML
    private Label labelUsername;

    @FXML
    private PieChart pieChartExpenseCategory;

    @FXML
    private PieChart pieChartIncomeCategory;

    @FXML
    private TextArea taExpenseDescription;

    @FXML
    private TextArea taIncomeDescription;

    @FXML
    private TextField tfExpenseAmount;

    @FXML
    private TextField tfExpenseCategory;

    @FXML
    private TextField tfExpenseTitle;

    @FXML
    private TextField tfIncomeAmount;

    @FXML
    private TextField tfIncomeCategory;

    @FXML
    private TextField tfIncomeTitle;

    @FXML
    private TableView<?> tvExpense;

    @FXML
    private TableView<?> tvIncome;

    @FXML
    void addExpense() {

    }

    @FXML
    void addIncome() {

    }

    @FXML
    void clearExpense() {

    }

    @FXML
    void clearIncome() {

    }

    @FXML
    void selectExpense() {

    }

    @FXML
    void selectIncome() {

    }

    @FXML
    void updateExpense() {

    }

    @FXML
    void updateIncome() {

    }


    @FXML
    void clickedLogout(ActionEvent event) {
        try {
            Alert alert;
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Баталгаажуулах");
            alert.setHeaderText(null);
            alert.setContentText("Та программаас гарахад итгэлтэй байна уу?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                btnLogout.getScene().getWindow().hide();
                Parent loginStageParent = FXMLLoader.load(getClass().getResource("login.fxml"));
                Stage loginStage = new Stage();
                Scene loginStageScene = new Scene(loginStageParent);
                loginStage.setScene(loginStageScene);
                loginStage.setTitle("Login");
                loginStage.setResizable(false);
                loginStage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchPane(ActionEvent event) {

    }

}
