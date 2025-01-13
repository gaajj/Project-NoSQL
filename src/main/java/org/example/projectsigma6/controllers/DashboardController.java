package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;

public class DashboardController {

    private MainApp mainApp;
    private Employee loggedInEmployee;

    @FXML
    private Label loggedInUsernameLabel;
    @FXML
    private Label loggedInFullNameLabel;
    @FXML
    private Label loggedInEmailLabel;

    public DashboardController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.loggedInEmployee = mainApp.getLoggedInEmployee();
    }

    @FXML
    public void initialize() {
        displayLoggedInUserInfo();
    }

    public void displayLoggedInUserInfo() {
        loggedInUsernameLabel.setText(loggedInEmployee.getUsername());
        loggedInFullNameLabel.setText(loggedInEmployee.getFirstName() + " " + loggedInEmployee.getLastName());
        loggedInEmailLabel.setText(loggedInEmployee.getEmail());
    }

}
