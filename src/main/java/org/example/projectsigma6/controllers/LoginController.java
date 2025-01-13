package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    public LoginController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    @FXML
    public void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (validateLogin(username, password)) {
            mainApp.showMainView();
        }
    }

    private boolean validateLogin(String username, String password) {
        Employee employee = employeeService.getEmployeeByUsername(username);

        if (employee == null) {
            System.out.println("[*] Invalid username.");
            showError();
            return false;
        }

        String storedHash = employee.getHashedPassword();
        String storedSalt = employee.getSalt();

        String inputHash = employeeService.hashPassword(password, storedSalt);

        if (inputHash.equals(storedHash)) {
            System.out.println("[*] Login successful for user: " + username);
            mainApp.setLoggedInEmployee(employee);
            return true;
        } else {
            System.out.println("[*] Invalid password.");
            showError();
            return false;
        }
    }

    private void showError() {
        errorLabel.setText("Invalid username or password.");
        errorLabel.setVisible(true);
    }

}
