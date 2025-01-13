package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

public class LoginController {

    private MainApp mainApp;
    private EmployeeService employeeService;

    @FXML
    private TextField usernameField;

    public LoginController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();

        Employee loggedInEmployee = employeeService.getEmployeeByUsername(username);

        if (loggedInEmployee != null) {
            mainApp.setLoggedInEmployee(loggedInEmployee);
            System.out.println("[*] Logged in as: " + loggedInEmployee.getUsername());

            mainApp.loadPage("DashboardView.fxml", new DashboardController(mainApp), null);
        } else {
            System.out.println("[*] Invalid username.");
        }
    }

}
