package com.example.personalfinanceapp.controller;

import com.example.personalfinanceapp.db.DatabaseConnection;
import com.example.personalfinanceapp.model.*;
import com.example.personalfinanceapp.util.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDashboardController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(UserDashboardController.class.getName());
    private static final String INSERT_QUERY_FORMAT = "INSERT INTO %s (title, category, description, amount, date)" +
            " VALUES (?,?,?,?,?)";
    private static final String DELETE_QUERY_FORMAT = "DELETE FROM %s WHERE id = ?";
    private static final String SELECT_QUERY_FORMAT = "SELECT * FROM %s";

    @FXML
    private BarChart<String, Number> barChartIncome;

    @FXML
    private BarChart<String, Number> barChartExpense;

    @FXML
    private CategoryAxis xAxisIncome;

    @FXML
    private CategoryAxis xAxisExpense;

    @FXML
    private NumberAxis yAxisIncome;

    @FXML
    private NumberAxis yAxisExpense;


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
    private DatePicker dpDashboardEndDate;

    @FXML
    private DatePicker dpDashboardStartDate;


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
    private TableColumn<Goal, Integer> colGoalActual;

    @FXML
    private TableColumn<Goal, Button> colGoalEdit;

    @FXML
    private TableColumn<Goal, Integer> colGoalExpected;

    @FXML
    private TableColumn<Goal, String> colGoalMonth;

    @FXML
    private TableColumn<Goal, Boolean> colGoalDone;

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private AnchorPane expensePane;

    @FXML
    private AnchorPane incomePane;
    @FXML
    private AnchorPane goalPane;

    @FXML
    private Label lblExpense;

    @FXML
    private Label lblIncome;

    @FXML
    private Label labelUsername;
    @FXML
    private Label lblGoalYear;

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
    private TextField tfGoalExpected;

    @FXML
    private TextField tfGoalMonth;

    @FXML
    private DatePicker dpExpenseDate;

    @FXML
    private DatePicker dpIncomeDate;

    @FXML
    private TableView<Expense> treevwExpense;
    @FXML
    private TableView<Goal> treevwGoal;

    @FXML
    private TableView<Income> treevwIncome;
    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private String selectedIncomeId;
    private String selectedExpenseId;
    private String selectedGoalId;


    public ObservableList<Income> incomeListData() {
        String query = String.format(SELECT_QUERY_FORMAT,"income");

        ObservableList<Income> incomeList = FXCollections.observableArrayList();

        connect = DatabaseConnection.getConnection();

        try {
            prepare = connect.prepareStatement(query);
            result = prepare.executeQuery();

            Income incomeData;
            while (result.next()) {
                incomeData = new Income(result.getString("id"), result.getString("title"),
                        result.getString("category"), result.getString("description"),
                        result.getInt("amount"), result.getDate("date"));
                incomeList.add(incomeData);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
        return incomeList;
    }

    public void incomeShowListData() {
        LOGGER.info("Entering incomeShowListData method.");
        ObservableList<Income> incomeList = incomeListData();
        colIncomeTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colIncomeCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colIncomeDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colIncomeAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colIncomeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colIncomeEdit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        colIncomeDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        Callback<TableColumn<Income, Button>, TableCell<Income, Button>> editCellFactory
                = new Callback<>() {
            @Override
            public TableCell<Income, Button> call(TableColumn<Income, Button> incomeDataStringTableColumn) {
                return new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/personalfinanceapp/images/edit.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Income incomeData = getTableView().getItems().get(getIndex());


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
            }
        };

        colIncomeEdit.setCellFactory(editCellFactory);

        Callback<TableColumn<Income, Button>, TableCell<Income, Button>> deleteCellFactory
                = new Callback<>() {
            @Override
            public TableCell<Income, Button> call(TableColumn<Income, Button> manageDataStringTableColumn) {
                return new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/personalfinanceapp/images/delete.png")));
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
            }
        };

        colIncomeDelete.setCellFactory(deleteCellFactory);

        treevwIncome.setItems(incomeList);
        LOGGER.info("Exiting incomeShowListData method.");
    }

    public ObservableList<Expense> expenseListData() {
        String query = String.format(SELECT_QUERY_FORMAT,"expense");

        ObservableList<Expense> expenseList = FXCollections.observableArrayList();

        connect = DatabaseConnection.getConnection();

        try {
            prepare = connect.prepareStatement(query);
            result = prepare.executeQuery();

            Expense expenseData;
            while (result.next()) {
                expenseData = new Expense(result.getString("id"), result.getString("title"),
                        result.getString("category"), result.getString("description"),
                        result.getInt("amount"), result.getDate("date"));
                expenseList.add(expenseData);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
        return expenseList;
    }

    public void expenseShowListData() {
        LOGGER.info("Entering expenseShowListData method.");
        ObservableList<Expense> expenseList = expenseListData();
        colExpenseTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colExpenseCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colExpenseDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colExpenseAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colExpenseDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colExpenseEdit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        colExpenseDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));

        Callback<TableColumn<Expense, Button>, TableCell<Expense, Button>> editCellFactory
                = new Callback<>() {
            @Override
            public TableCell<Expense, Button> call(TableColumn<Expense, Button> expenseDataStringTableColumn) {
                return new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/personalfinanceapp/images/edit.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Expense expenseData = getTableView().getItems().get(getIndex());


                                selectedExpenseId = expenseData.getId();
                                dpExpenseDate.setDisable(false);

                                tfExpenseTitle.setText(String.valueOf(expenseData.getTitle()));
                                tfExpenseCategory.setText(String.valueOf(expenseData.getCategory()));
                                taExpenseDescription.setText(String.valueOf(expenseData.getDescription()));
                                tfExpenseAmount.setText(String.valueOf(expenseData.getAmount()));
                                dpExpenseDate.setValue(LocalDate.parse(String.valueOf(expenseData.getDate())));
                                if (!tfExpenseTitle.getText().equals("")) {
                                    tfExpenseCategory.setEditable(true);
                                    tfExpenseAmount.setEditable(true);
                                    taExpenseDescription.setEditable(true);
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };

        colExpenseEdit.setCellFactory(editCellFactory);

        Callback<TableColumn<Expense, Button>, TableCell<Expense, Button>> deleteCellFactory
                = new Callback<>() {
            @Override
            public TableCell<Expense, Button> call(TableColumn<Expense, Button> manageDataStringTableColumn) {
                return new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/personalfinanceapp/images/delete.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Expense expenseData = getTableView().getItems().get(getTableRow().getIndex());
                                deleteExpense(expenseData);
                                clearExpense();
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };

        colExpenseDelete.setCellFactory(deleteCellFactory);

        treevwExpense.setItems(expenseList);
        LOGGER.info("Exiting incomeShowListData method.");
    }

    public ObservableList<Goal> goalListData() {
        String query = String.format(SELECT_QUERY_FORMAT,"goal");

        ObservableList<Goal> goalList = FXCollections.observableArrayList();

        connect = DatabaseConnection.getConnection();

        try {
            prepare = connect.prepareStatement(query);
            result = prepare.executeQuery();

            Goal goalData;
            while (result.next()) {

                goalData = new Goal(result.getString("Id"), result.getString("month"),
                        result.getInt("expected"), result.getInt("actual"), result.getBoolean("done"));
                goalList.add(goalData);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        } finally {
            try {
                if (result != null) {
                    result.close();
                }
                if (prepare != null) {
                    prepare.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return goalList;
    }

    public void goalShowListData() {
        LOGGER.info("Entering goalShowListData method.");
        ObservableList<Goal> goalList = goalListData();
        colGoalMonth.setCellValueFactory(new PropertyValueFactory<>("month"));
        colGoalExpected.setCellValueFactory(new PropertyValueFactory<>("expected"));
        colGoalActual.setCellValueFactory(new PropertyValueFactory<>("actual"));
        colGoalEdit.setCellValueFactory(new PropertyValueFactory<>("edit"));
        colGoalDone.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                // Add a listener to update the CheckBox when the item changes
                itemProperty().addListener((obs, oldValue, newValue) -> {
                    if (newValue != null) {
                        int actual = getTableView().getItems().get(getIndex()).getActual();
                        int expected = getTableView().getItems().get(getIndex()).getExpected();
                        checkBox.setSelected(actual >= expected);
                    } else {
                        checkBox.setSelected(false);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty) {
                    setGraphic(checkBox);
                } else {
                    setGraphic(null);
                }
            }
        });

        Callback<TableColumn<Goal, Button>, TableCell<Goal, Button>> editCellFactory
                = new Callback<>() {
            @Override
            public TableCell<Goal, Button> call(TableColumn<Goal, Button> goalDaStringTableColumn) {
                return new TableCell<>() {
                    final Button btn = new Button();

                    @Override
                    public void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            Image imgEdit = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/example/personalfinanceapp/images/edit.png")));
                            ImageView viewEdit = new ImageView(imgEdit);
                            viewEdit.setFitHeight(16);
                            viewEdit.setPreserveRatio(true);
                            btn.setPrefSize(16, 16);
                            btn.setGraphic(viewEdit);
                            btn.setOnAction(event -> {
                                Goal goalData = getTableView().getItems().get(getIndex());

                                selectedGoalId = goalData.getId();

                                tfGoalMonth.setText(String.valueOf(goalData.getMonth()));
                                tfGoalExpected.setText(String.valueOf(goalData.getExpected()));
                                if (!tfGoalMonth.getText().equals("")) {
                                    tfGoalExpected.setEditable(true);

                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };

        colGoalEdit.setCellFactory(editCellFactory);

        treevwGoal.setItems(goalList);
        LOGGER.info("Exiting goalShowListData method.");
    }
    @FXML
    void addExpense(ActionEvent event) {
        addRecord("expense", tfExpenseTitle, tfExpenseCategory, taExpenseDescription, tfExpenseAmount, dpExpenseDate);
    }

    @FXML
    void addIncome(ActionEvent event) {
        addRecord("income", tfIncomeTitle, tfIncomeCategory, taIncomeDescription, tfIncomeAmount, dpIncomeDate);
    }
    private void addRecord(String tableName, TextField titleField, TextField categoryField,
                           TextArea descriptionArea, TextField amountField, DatePicker datePicker) {
        String query = String.format(INSERT_QUERY_FORMAT, tableName);
        connect = DatabaseConnection.getConnection();
        try {
            if (titleField.getText().isEmpty() || categoryField.getText().isEmpty() ||
                    descriptionArea.getText().isEmpty() || amountField.getText().isEmpty() || datePicker.getValue() == null) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", null, "Fill in the missing fields.");
            } else {
                LocalDate recordDate = datePicker.getValue();
                java.sql.Date sqlDate = java.sql.Date.valueOf(recordDate);

                prepare = connect.prepareStatement(query);
                prepare.setString(1, titleField.getText());
                prepare.setString(2, categoryField.getText());
                prepare.setString(3, descriptionArea.getText());
                prepare.setInt(4, Integer.parseInt(amountField.getText()));
                prepare.setString(5, String.valueOf(sqlDate));

                prepare.executeUpdate();

                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, tableName + " successfully added.");

                if (tableName.equals("expense")) {
                    expenseShowListData();
                    clearExpense();
                } else if (tableName.equals("income")) {
                    incomeShowListData();
                    clearIncome();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    @FXML
    void addGoal(ActionEvent event) {
        String query = "INSERT INTO goal (month, expected, actual,done) " + "VALUES (?,?,?,?)";
        connect = DatabaseConnection.getConnection();
        try {
            if (tfGoalMonth.getText().isEmpty() || tfGoalExpected.getText().isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", null, "Fill in the missing fields.");
            } else {
                int calculatedActual = calculateActualForGoal(tfGoalMonth.getText());

                prepare = connect.prepareStatement(query);
                prepare.setString(1, tfGoalMonth.getText());
                prepare.setInt(2, Integer.parseInt(tfGoalExpected.getText()));
                prepare.setInt(3, calculatedActual);
                prepare.setBoolean(4, (calculatedActual > Integer.parseInt(tfGoalExpected.getText())));

                prepare.executeUpdate();


                AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, "Goal successfully added.");

                goalShowListData();
                clearGoal();

            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    public void deleteExpense(Expense expenseData) {
        deleteRecord("expense", expenseData);
    }

    public void deleteIncome(Income incomeData) {
        deleteRecord("income", incomeData);
    }

    public <T extends RecordData> void deleteRecord(String tableName, T recordData) {
        String query = String.format(DELETE_QUERY_FORMAT, tableName);
        connect = DatabaseConnection.getConnection();

        try {
            if (recordData != null) {

                String recordType = (tableName.equals("expense")) ? "expense" : "income";

                if (AlertUtils.showConfirmationAlert("Confirmation", null, "Are you sure you want to delete " + recordType + " : " + recordData.getTitle() + " ?")) {
                    prepare = connect.prepareStatement(query);
                    prepare.setString(1, recordData.getId());

                    prepare.executeUpdate();

                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, recordType + " successfully deleted.");

                    if (tableName.equals("expense")) {
                        expenseShowListData();
                        clearExpense();
                    } else if (tableName.equals("income")) {
                        incomeShowListData();
                        clearExpense();
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);;
        }
    }


    @FXML
    void clearExpense() {
        tfExpenseTitle.clear();
        tfExpenseCategory.clear();
        tfExpenseAmount.clear();
        taExpenseDescription.clear();
        dpExpenseDate.setValue(null);
        tfExpenseCategory.setEditable(true);
        taExpenseDescription.setEditable(true);
        tfExpenseAmount.setEditable(true);
        dpExpenseDate.setEditable(true);
        dpExpenseDate.setDisable(false);
    }

    @FXML
    void clearIncome() {
        tfIncomeTitle.clear();
        tfIncomeCategory.clear();
        tfIncomeAmount.clear();
        taIncomeDescription.clear();
        dpIncomeDate.setValue(null);
        tfIncomeCategory.setEditable(true);
        taIncomeDescription.setEditable(true);
        tfIncomeAmount.setEditable(true);
        dpIncomeDate.setEditable(true);
        dpIncomeDate.setDisable(false);
    }

    @FXML
    void clearGoal() {
        tfGoalMonth.clear();
        tfGoalExpected.clear();
    }

    @FXML
    void selectExpense() {
        Expense expenseData = treevwExpense.getSelectionModel().getSelectedItem();
        int num = treevwExpense.getSelectionModel().getSelectedIndex();

        if (num < 0)
            return;
        tfExpenseTitle.setText(String.valueOf(expenseData.getTitle()));
        tfExpenseCategory.setText(String.valueOf(expenseData.getCategory()));
        taExpenseDescription.setText(String.valueOf(expenseData.getDescription()));
        tfExpenseAmount.setText(String.valueOf(expenseData.getAmount()));
        dpExpenseDate.setValue(LocalDate.parse(String.valueOf(expenseData.getDate())));
        if (!tfExpenseTitle.getText().equals("")) {
            tfExpenseCategory.setEditable(false);
            taExpenseDescription.setEditable(false);
            tfExpenseAmount.setEditable(false);
            dpExpenseDate.setEditable(false);
            dpExpenseDate.setDisable(true);
        }
    }

    @FXML
    void selectIncome() {
        Income incomeData = treevwIncome.getSelectionModel().getSelectedItem();
        int num = treevwIncome.getSelectionModel().getSelectedIndex();

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
    void selectGoal() {
        Goal goalData = treevwGoal.getSelectionModel().getSelectedItem();
        int num = treevwGoal.getSelectionModel().getSelectedIndex();

        if (num < 0)
            return;
        tfGoalMonth.setText(String.valueOf(goalData.getMonth()));
        tfGoalExpected.setText(String.valueOf(goalData.getExpected()));

        if (!tfGoalMonth.getText().equals("")) {
            tfGoalExpected.setEditable(false);
        }
    }

    @FXML
    void updateExpense() {
        String query = "UPDATE expense SET title = ?, category = ?, description = ?, amount = ?, date = ? WHERE id = ?";

        connect = DatabaseConnection.getConnection();

        try {
            if (tfExpenseTitle.getText().isEmpty() || tfExpenseCategory.getText().isEmpty() || taExpenseDescription.getText().isEmpty() ||
                    tfExpenseAmount.getText().isEmpty() || dpExpenseDate.getValue() == null) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", null, "Fill in the missing fields.");

            } else {
                if (AlertUtils.showConfirmationAlert("Confirmation", null, "Are you sure you want to update this?")) {
                    LocalDate expenseDate = dpExpenseDate.getValue();
                    java.sql.Date sqlDate = java.sql.Date.valueOf(expenseDate);

                    prepare = connect.prepareStatement(query);
                    prepare.setString(1, tfExpenseTitle.getText());
                    prepare.setString(2, tfExpenseCategory.getText());
                    prepare.setString(3, taExpenseDescription.getText());
                    prepare.setString(4, tfExpenseAmount.getText());
                    prepare.setDate(5, sqlDate);
                    prepare.setString(6, selectedExpenseId);

                    prepare.executeUpdate();

                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, "Successfully updated.");

                    expenseShowListData();
                    clearExpense();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    @FXML
    void updateIncome() {
        String query = "UPDATE income SET title = ?, category = ?, description = ?, amount = ?, date = ? WHERE id = ?";

        connect = DatabaseConnection.getConnection();

        try {
            if (tfIncomeTitle.getText().isEmpty() || tfIncomeCategory.getText().isEmpty() || taIncomeDescription.getText().isEmpty() ||
                    tfIncomeAmount.getText().isEmpty() || dpIncomeDate.getValue() == null) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", null, "Fill in the missing fields.");

            } else {
                if (AlertUtils.showConfirmationAlert("Confirmation", null, "Are you sure you want to update this?")) {
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

                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, "Successfully updated.");


                    incomeShowListData();
                    clearIncome();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    private int calculateActualForGoal(String month) {

        Map<String, String> monthMap = new HashMap<>();
        monthMap.put("January", "01");
        monthMap.put("February", "02");
        monthMap.put("March", "03");
        monthMap.put("April", "04");
        monthMap.put("May", "05");
        monthMap.put("June", "06");
        monthMap.put("July", "07");
        monthMap.put("August", "08");
        monthMap.put("September", "09");
        monthMap.put("October", "10");
        monthMap.put("November", "11");
        monthMap.put("December", "12");

        String monthNumber = monthMap.get(month);

        String formattedMonth = LocalDate.now().getYear() + "-" + monthNumber;

        LocalDate startDate = LocalDate.parse(formattedMonth + "-01");
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        long totalIncome = calculateTotalIncomeFromDatabase(startDate, endDate);
        long totalExpense = calculateTotalExpenseFromDatabase(startDate, endDate);

        return (int) (totalIncome - totalExpense);
    }

    @FXML
    void updateGoal() {
        String query = "UPDATE goal SET month = ?, expected = ?, actual = ?, done = ? WHERE id = ?";

        connect = DatabaseConnection.getConnection();

        try {
            if (tfGoalMonth.getText().isEmpty() || tfGoalExpected.getText().isEmpty()) {
                AlertUtils.showAlert(Alert.AlertType.ERROR, "Error", null, "Fill in the missing fields.");

            } else {
                if (AlertUtils.showConfirmationAlert("Confirmation", null, "Are you sure you want to update this?")) {
                    int calculatedActual = calculateActualForGoal(tfGoalMonth.getText());

                    prepare = connect.prepareStatement(query);
                    prepare.setString(1, tfGoalMonth.getText());
                    prepare.setInt(2, Integer.parseInt(tfGoalExpected.getText()));
                    prepare.setInt(3, calculatedActual);
                    prepare.setBoolean(4, (calculatedActual > Integer.parseInt(tfGoalExpected.getText())));
                    prepare.setString(5, selectedGoalId);

                    prepare.executeUpdate();

                    AlertUtils.showAlert(Alert.AlertType.INFORMATION, "Success", null, "Successfully updated.");

                    goalShowListData();
                    clearGoal();
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    public long calculateTotalIncomeFromDatabase(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT amount FROM income WHERE date BETWEEN ? AND ?";
        long totalIncome = 0;

        try {
            if (connect == null || connect.isClosed()) {
                connect = DatabaseConnection.getConnection();
            }

            prepare = connect.prepareStatement(query);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();

            while (result.next()) {
                totalIncome += result.getLong("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totalIncome;
    }

    public long calculateTotalExpenseFromDatabase(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT amount FROM expense WHERE date BETWEEN ? AND ?";
        long totalExpense = 0;

        try {
            if (connect == null || connect.isClosed()) {
                connect = DatabaseConnection.getConnection();
            }

            prepare = connect.prepareStatement(query);
            prepare.setDate(1, java.sql.Date.valueOf(startDate));
            prepare.setDate(2, java.sql.Date.valueOf(endDate));
            result = prepare.executeQuery();

            while (result.next()) {
                totalExpense += result.getLong("amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (result != null) result.close();
                if (prepare != null) prepare.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return totalExpense;
    }


    @FXML
    void clickedLogout(ActionEvent event) {
        try {
            if (AlertUtils.showConfirmationAlert("Confirmation", null, "Are you sure you want to log out?")) {
                btnLogout.getScene().getWindow().hide();
                Parent loginStageParent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/personalfinanceapp/login.fxml")));
                Stage loginStage = new Stage();
                Scene loginStageScene = new Scene(loginStageParent);
                loginStage.setScene(loginStageScene);
                loginStage.setTitle("Login");
                loginStage.setResizable(false);
                loginStage.show();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An unexpected error occurred while adding a record", e);
        }
    }

    @FXML
    void switchPane(ActionEvent event) {
        if (event.getSource() == btnDashboard) {
            dashboardPane.setVisible(true);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
            goalPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: #aab6fb;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");

            showByThisYear(event);
        } else if (event.getSource() == btnExpense) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(true);
            incomePane.setVisible(false);
            goalPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: transparent");
            btnExpense.setStyle("-fx-background-color:  #aab6fb;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: transparent;");

            expenseShowListData();
        } else if (event.getSource() == btnIncome) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(true);
            goalPane.setVisible(false);

            btnDashboard.setStyle("-fx-background-color: transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: #aab6fb;");
            btnGoals.setStyle("-fx-background-color: transparent;");

            incomeShowListData();
        } else if (event.getSource() == btnGoals) {
            dashboardPane.setVisible(false);
            expensePane.setVisible(false);
            incomePane.setVisible(false);
            goalPane.setVisible(true);

            btnDashboard.setStyle("-fx-background-color:  transparent;");
            btnExpense.setStyle("-fx-background-color: transparent;");
            btnIncome.setStyle("-fx-background-color: transparent;");
            btnGoals.setStyle("-fx-background-color: #aab6fb;");
            goalShowListData();
        }
    }

    @FXML
    private void showByFilteredDate() {
        LocalDate startDate = dpDashboardStartDate.getValue();
        LocalDate endDate = dpDashboardEndDate.getValue();

        if (startDate == null || endDate == null) {
            AlertUtils.showAlert(Alert.AlertType.WARNING, "Warning", null, "Please enter both start date and end date.");

        } else {
            long totalIncome = calculateTotalIncomeFromDatabase(startDate, endDate);
            long totalExpense = calculateTotalExpenseFromDatabase(startDate, endDate);

            lblIncome.setText(String.valueOf(totalIncome));
            lblExpense.setText(String.valueOf(totalExpense));

            populateIncomeDataFromDatabase(startDate, endDate);
            populateExpenseDataFromDatabase(startDate, endDate);

            populateExpenseCategoryPieChart(startDate, endDate);
            populateIncomeCategoryPieChart(startDate, endDate);

        }
    }

    @FXML
    void showByThisYear(ActionEvent event) {
        dpDashboardStartDate.setValue(null);
        dpDashboardEndDate.setValue(null);
        LocalDate defaultStartDate = LocalDate.now().withDayOfYear(1);

        long totalIncome = calculateTotalIncomeFromDatabase(defaultStartDate, LocalDate.now());
        long totalExpense = calculateTotalExpenseFromDatabase(defaultStartDate, LocalDate.now());

        lblIncome.setText(String.valueOf(totalIncome));
        lblExpense.setText(String.valueOf(totalExpense));

        populateIncomeDataFromDatabase(defaultStartDate, LocalDate.now());
        populateExpenseDataFromDatabase(defaultStartDate, LocalDate.now());

        populateExpenseCategoryPieChart(defaultStartDate, LocalDate.now());
        populateIncomeCategoryPieChart(defaultStartDate, LocalDate.now());

    }

    private void populateIncomeDataFromDatabase(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total " +
                "FROM income " +
                "WHERE date BETWEEN ? AND ? " +
                "GROUP BY month";

        populateChartDataFromDatabase(query, barChartIncome, startDate, endDate);

        barChartIncome.setTitle("Income Overview");
        xAxisIncome.setLabel("Months");
        yAxisIncome.setLabel("Amount (₮)");
    }

    private void populateExpenseDataFromDatabase(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(amount) AS total " +
                "FROM expense " +
                "WHERE date BETWEEN ? AND ? " +
                "GROUP BY month";

        populateChartDataFromDatabase(query, barChartExpense, startDate, endDate);

        barChartExpense.setTitle("Expense Overview");
        xAxisExpense.setLabel("Months");
        yAxisExpense.setLabel("Amount (₮)");
    }

    private void populateChartDataFromDatabase(String query, BarChart<String, Number> barChart,
                                               LocalDate startDate, LocalDate endDate) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            if (startDate != null && endDate != null) {
                statement.setDate(1, java.sql.Date.valueOf(startDate));
                statement.setDate(2, java.sql.Date.valueOf(endDate));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                while (resultSet.next()) {
                    String month = resultSet.getString("month");
                    double total = resultSet.getDouble("total");
                    series.getData().add(new XYChart.Data<>(month, total));
                }

                barChart.getData().clear();

                barChart.getData().add(series);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateExpenseCategoryPieChart(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT category, SUM(amount) AS total " +
                "FROM expense " +
                "WHERE date BETWEEN ? AND ? " +
                "GROUP BY category";

        populateCategoryPieChartFromDatabase(query, pieChartExpenseCategory, startDate, endDate);

        pieChartExpenseCategory.setTitle("Expense Categories Overview");
    }

    private void populateIncomeCategoryPieChart(LocalDate startDate, LocalDate endDate) {
        String query = "SELECT category, SUM(amount) AS total " +
                "FROM income " +
                "WHERE date BETWEEN ? AND ? " +
                "GROUP BY category";

        populateCategoryPieChartFromDatabase(query, pieChartIncomeCategory, startDate, endDate);

        pieChartIncomeCategory.setTitle("Income Categories Overview");
    }

    private void populateCategoryPieChartFromDatabase(String query, PieChart pieChart,
                                                      LocalDate startDate, LocalDate endDate) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {


            if (startDate != null && endDate != null) {
                statement.setDate(1, java.sql.Date.valueOf(startDate));
                statement.setDate(2, java.sql.Date.valueOf(endDate));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                pieChart.getData().clear();

                while (resultSet.next()) {
                    String category = resultSet.getString("category");
                    double total = resultSet.getDouble("total");
                    PieChart.Data data = new PieChart.Data(category, total);
                    pieChart.getData().add(data);
                }


                pieChart.setLabelLineLength(10);
                pieChart.setLegendVisible(true);
                pieChart.setStartAngle(90);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnDashboard.setStyle("-fx-background-color: #aab6fb;");
        LocalDate defaultStartDate = LocalDate.now().minusMonths(1);
        dpDashboardStartDate.setValue(defaultStartDate);
        dpDashboardEndDate.setValue(LocalDate.now());

        long totalIncome = calculateTotalIncomeFromDatabase(defaultStartDate, LocalDate.now());
        long totalExpense = calculateTotalExpenseFromDatabase(defaultStartDate, LocalDate.now());

        lblIncome.setText(String.valueOf(totalIncome));
        lblExpense.setText(String.valueOf(totalExpense));

        populateIncomeDataFromDatabase(defaultStartDate, LocalDate.now());
        populateExpenseDataFromDatabase(defaultStartDate, LocalDate.now());

        populateExpenseCategoryPieChart(defaultStartDate, LocalDate.now());
        populateIncomeCategoryPieChart(defaultStartDate, LocalDate.now());

        labelUsername.setText(UserContext.getUsername().toUpperCase());
    }
}