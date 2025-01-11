package org.example.projectsigma6.controllers;

import org.example.projectsigma6.services.EmployeeService;

public class LoginController {

    private EmployeeService employeeService;

    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public void initialize() {

    }

}
