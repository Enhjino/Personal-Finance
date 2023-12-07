package com.example.personalfinanceapp;

import com.example.personalfinanceapp.model.Expense;
import com.example.personalfinanceapp.model.Income;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserDashboardController implements Initializable {

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
    private TableColumn<Expense, Integer> colExpenseAmount;

    @FXML
    private TableColumn<Expense, String> colExpenseCategory;

    @FXML
    private TableColumn<Expense, Date> colExpenseDate;

    @FXML
    private TableColumn<Expense, Button> colExpenseEdit;

    @FXML
    private TableColumn<Expense, Button> colExpenseDelete;

    @FXML
    private TableColumn<Expense, String> colExpenseDescription;


    @FXML
    private TableColumn<Expense, String> colExpenseTitle;

    @FXML
    private TableColumn<Income, Integer> colIncomeAmount;

    @FXML
    private TableColumn<Income, String> colIncomeCategory;

    @FXML
    private TableColumn<Income, Date> colIncomeDate;
    @FXML
    private TableColumn<Income, Button> colIncomeEdit;

    @FXML
    private TableColumn<Income, Button> colIncomeDelete;

    @FXML
    private TableColumn<Income, String> colIncomeDescription;

    @FXML
    private TableColumn<Income, String> colIncomeTitle;

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
    private DatePicker dpExpenseDate;

    @FXML
    private DatePicker dpIncomeDate;

    @FXML
    private TableView<Expense> tvExpense;

    @FXML
    private TableView<Income> tvIncome;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;
    private String selectedIncomeId;
    private String selectedExpenseId;

    /* Customer */
    public ObservableList<Income> incomeListData() {
        String query = "SELECT * FROM income";

        ObservableList<Income> incomeList = FXCollections.observableArrayList();

        connect = DatabaseConnection.getConnection();

        try {
            prepare = connect.prepareStatement(query);
            result = prepare.executeQuery();

            Income incomeData;
            while (result.next()) {
                incomeData = new Income(result.getString("id"), result.getString("title"),
                        result.getString("category"), result.getString("description"),
                        result.getInt("amount"),  result.getDate("date"));
                incomeList.add(incomeData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return incomeList;
    }
    private ObservableList<Income> incomeList;

    public void incomeShowListData() {
        incomeList = incomeListData();
        colIncomeTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colIncomeCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colIncomeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colIncomeAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colIncomeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colIncomeEdit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        colIncomeDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        Callback<TableColumn<Income, Button>, TableCell<Income, Button>> editCellFactory
                = new Callback<TableColumn<Income, Button>, TableCell<Income, Button>>() {
            @Override
            public TableCell<Income, Button> call(TableColumn<Income, Button> incomeDataStringTableColumn) {
                final TableCell<Income, Button> cell = new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/edit.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Income incomeData = getTableView().getItems().get(getIndex());

                                // Set the selected income ID
                                selectedIncomeId = incomeData.getId();
                                dpIncomeDate.setDisable(false);

                                tfIncomeTitle.setText(String.valueOf(incomeData.getTitle()));
                                tfIncomeCategory.setText(String.valueOf(incomeData.getCategory()));
                                taIncomeDescription.setText(String.valueOf(incomeData.getDescription()));
                                tfIncomeAmount.setText(String.valueOf(incomeData.getAmount()));
                                dpIncomeDate.setValue(LocalDate.parse(String.valueOf(incomeData.getDate())));
                                if (!tfIncomeTitle.getText().equals("")) {
                                    tfIncomeCategory.setEditable(true);
                                    tfIncomeAmount.setEditable(true);
                                    taIncomeDescription.setEditable(true);
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        colIncomeEdit.setCellFactory(editCellFactory);

        Callback<TableColumn<Income, Button>, TableCell<Income, Button>> deleteCellFactory
                = new Callback<TableColumn<Income, Button>, TableCell<Income, Button>>() {
            @Override
            public TableCell<Income, Button> call(TableColumn<Income, Button> manageDataStringTableColumn) {
                final TableCell<Income, Button> cell = new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("images/delete.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Income incomeData = getTableView().getItems().get(getTableRow().getIndex());
                                deleteIncome(incomeData);
                                clearIncome();
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        colIncomeDelete.setCellFactory(deleteCellFactory);

        tvIncome.setItems(incomeList);
    }

    @FXML
    void addExpense() {

    }

    @FXML
    void addIncome() {
        String query = "INSERT INTO income (title, category, description, amount, date) " + "VALUES (?,?,?,?,?)";
        connect = DatabaseConnection.getConnection();
        try {
            Alert alert;
            if (tfIncomeTitle.getText().isEmpty() || tfIncomeCategory.getText().isEmpty() || taIncomeDescription.getText().isEmpty() ||
                    tfIncomeAmount.getText().isEmpty() || dpIncomeDate.getValue() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Алдаа");
                alert.setHeaderText(null);
                alert.setContentText("Дутуу талбаруудыг нөхнө үү.");
                alert.showAndWait();
            } else {
                LocalDate incomeDate = dpIncomeDate.getValue();
                java.sql.Date sqlDate = java.sql.Date.valueOf(incomeDate);

                prepare = connect.prepareStatement(query);
                prepare.setString(1, tfIncomeTitle.getText());
                prepare.setString(2, tfIncomeCategory.getText());
                prepare.setString(3, taIncomeDescription.getText());
                prepare.setInt(4, Integer.parseInt(tfIncomeAmount.getText()));
                prepare.setString(5, String.valueOf(sqlDate));

                prepare.executeUpdate();

                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Мэдэгдэл");
                alert.setHeaderText(null);
                alert.setContentText("Амжилттай нэмлээ.");
                alert.showAndWait();

                incomeShowListData();
                clearIncome();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteIncome(Income incomeData) {
        String deletePharmacist = "DELETE FROM income WHERE id = ?";

        connect = DatabaseConnection.getConnection();

        try {
            Alert alert;
            if (incomeData != null){
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Мэдэгдэл");
                alert.setHeaderText(null);
                alert.setContentText("Та Income Id: " + incomeData.getId() + "-г устгахдаа итгэлтэй байна уу?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(deletePharmacist);
                    prepare.setString(1, incomeData.getId());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Мэдэгдэл");
                    alert.setHeaderText(null);
                    alert.setContentText("Амжилттай устгагдлаа.");
                    alert.showAndWait();

                    incomeShowListData();
                    clearExpense();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clearExpense() {
        tfExpenseTitle.clear();
        tfExpenseCategory.clear();
        tfExpenseAmount.clear();
        taExpenseDescription.clear();
        dpExpenseDate.setValue(null);
    }

    @FXML
    void clearIncome() {
        tfIncomeTitle.clear();
        tfIncomeCategory.clear();
        tfIncomeAmount.clear();
        taIncomeDescription.clear();
        dpIncomeDate.setValue(null);
    }

    @FXML
    void selectExpense() {

    }

    @FXML
    void selectIncome() {
        Income incomeData = tvIncome.getSelectionModel().getSelectedItem();
        int num = tvIncome.getSelectionModel().getSelectedIndex();

        if (num < 0)
            return;
        tfIncomeTitle.setText(String.valueOf(incomeData.getTitle()));
        tfIncomeCategory.setText(String.valueOf(incomeData.getCategory()));
        taIncomeDescription.setText(String.valueOf(incomeData.getDescription()));
        tfIncomeAmount.setText(String.valueOf(incomeData.getAmount()));
        dpIncomeDate.setValue(LocalDate.parse(String.valueOf(incomeData.getDate())));
        if (!tfIncomeTitle.getText().equals("")) {
            tfIncomeCategory.setEditable(false);
            taIncomeDescription.setEditable(false);
            tfIncomeAmount.setEditable(false);
            dpIncomeDate.setEditable(false);
            dpIncomeDate.setDisable(true);
        }
    }

    @FXML
    void updateExpense() {

    }

    @FXML
    void updateIncome() {
        String query = "UPDATE income SET title = ?, category = ?, description = ?, amount = ?, date = ? WHERE id = ?";

        connect = DatabaseConnection.getConnection();

        try {
            Alert alert;
            if (tfIncomeTitle.getText().isEmpty() || tfIncomeCategory.getText().isEmpty() || taIncomeDescription.getText().isEmpty() ||
                    tfIncomeAmount.getText().isEmpty() || dpIncomeDate.getValue() == null ) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Алдаа");
                alert.setHeaderText(null);
                alert.setContentText("Дутуу талбаруудыг нөхнө үү.");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Мэдэгдэл");
                alert.setHeaderText(null);
                alert.setContentText("Та шинэчлэл хийхдээ итгэлтэй байна уу?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    LocalDate incomeDate = dpIncomeDate.getValue();
                    java.sql.Date sqlDate = java.sql.Date.valueOf(incomeDate);

                    prepare = connect.prepareStatement(query);
                    prepare.setString(1, tfIncomeTitle.getText());
                    prepare.setString(2, tfIncomeCategory.getText());
                    prepare.setString(3, taIncomeDescription.getText());
                    prepare.setString(4, tfIncomeAmount.getText());
                    prepare.setDate(5, sqlDate);
                    prepare.setString(6, selectedIncomeId);

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Мэдэгдэл");
                    alert.setHeaderText(null);
                    alert.setContentText("Амжилттай шинэчлэгдлээ.");
                    alert.showAndWait();

                    incomeShowListData();
                    clearIncome();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long calculateTotalIncomeFromDatabase() {
        String query = "SELECT amount FROM income";
        long totalIncome = 0;

        try {
            // Ensure that the database connection is established
            if (connect == null || connect.isClosed()) {
                connect = DatabaseConnection.getConnection();
            }

            statement = connect.createStatement();
            result = statement.executeQuery(query);

            while (result.next()) {
                totalIncome += result.getLong("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totalIncome;
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
    void updateTotalIncomeLabel() {
        long totalIncome = calculateTotalIncomeFromDatabase();

        labelIncome.setText(String.valueOf(totalIncome));
    }
    @FXML
    void switchPane(ActionEvent event) {
        if (event.getSource() == btnDashboard) {
            dashboardPane.setVisible(true);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
//            budgetPane.setVisible(false);
//            GoalPane.setVisible(false);
//            ReportPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: #aab6fb;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnBudget.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");
            btnReport.setStyle("-fx-background-color: transparent;");

        } else if (event.getSource() == btnExpense) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(true);
            incomePane.setVisible(false);
//            budgetPane.setVisible(false);
//            GoalPane.setVisible(false);
//            ReportPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: transparent");
            btnExpense.setStyle("-fx-background-color:  #aab6fb;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnBudget.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");
            btnReport.setStyle("-fx-background-color: transparent;");

        } else if (event.getSource() == btnIncome) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(true);
//            budgetPane.setVisible(false);
//            GoalPane.setVisible(false);
//            ReportPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: #aab6fb;");
            btnBudget.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");
            btnReport.setStyle("-fx-background-color: transparent;");

            incomeShowListData();
        } else if (event.getSource() == btnBudget) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
//            budgetPane.setVisible(true);
//            GoalPane.setVisible(false);
//            ReportPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnBudget.setStyle("-fx-background-color: #aab6fb;");
            btnGoals.setStyle("-fx-background-color: transparent;");
            btnReport.setStyle("-fx-background-color: transparent;");

        } else if (event.getSource() == btnGoals) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
//            budgetPane.setVisible(false);
//            GoalPane.setVisible(true);
//            ReportPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color:  transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnBudget.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: #aab6fb;");
            btnReport.setStyle("-fx-background-color: transparent;");

        } else if (event.getSource() == btnReport) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
//            budgetPane.setVisible(false);
//            GoalPane.setVisible(false);
//            ReportPane.setVisible(true);

            btnDashboard.setStyle("-fx-background-color:  transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnBudget.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");
            btnReport.setStyle("-fx-background-color: #aab6fb;");

        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDashboard.setStyle("-fx-background-color: #aab6fb;");
        updateTotalIncomeLabel();
    }
}
