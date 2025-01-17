package org.example.projectsigma6.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.Location;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

import java.util.List;

public class EmployeesViewController {

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, String> columnId;

    @FXML
    private TableColumn<Employee, String> columnUsername;

    @FXML
    private TableColumn<Employee, String> columnFullName;

    @FXML
    private TableColumn<Employee, String> columnEmail;

    @FXML
    private TableColumn<Employee, String> columnPhoneNumber;

    @FXML
    private TableColumn<Employee, String> columnEmployeeType;

    @FXML
    private TableColumn<Employee, String> columnLocation;

    @FXML
    private Label totalEmployeesLabel;

    private ObservableList<Employee> employeeList;
    private MainApp mainApp;
    private final EmployeeService employeeService;

    public EmployeesViewController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    @FXML
    public void initialize() {
        columnId.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getId().toString()));
        columnUsername.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUsername()));
        columnFullName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getFullName()));
        columnEmail.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEmail()));
        columnPhoneNumber.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPhoneNumber()));
        columnEmployeeType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getEmployeeType().toString()));
        columnLocation.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLocation().toString()));

        loadEmployees();
    }

    private void loadEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        employeeList = FXCollections.observableArrayList(employees);
        employeeTable.setItems(employeeList);

        updateTotalEmployeesLabel();
    }

    private void updateTotalEmployeesLabel() {
        totalEmployeesLabel.setText(String.valueOf(employeeList.size()));
    }

    @FXML
    private void handleAddEmployee() {
        mainApp.show("AddEditEmployeeView.fxml", new AddEditEmployeeController(mainApp));
    }

    @FXML
    private void handleEditEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            mainApp.show("AddEditEmployeeView.fxml", new AddEditEmployeeController(mainApp, selectedEmployee));
        } else {
            showAlert("No Employee Selected", "Please select an employee to edit.");
        }
    }

    @FXML
    private void handleDeleteEmployee() {
        Employee selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            employeeService.removeEmployee(selectedEmployee);
            employeeList.remove(selectedEmployee);
            updateTotalEmployeesLabel();
        } else {
            showAlert("No Employee Selected", "Please select an employee to delete.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
