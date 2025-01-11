package org.example.projectsigma6.controllers;

import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

public class LoginController {

    private MainApp mainApp;
    private EmployeeService employeeService;

    public LoginController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    public void initialize() {

    }

}
