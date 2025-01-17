package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.projectsigma6.MainApp;

public class MainController {

    private MainApp mainApp;

    @FXML
    private Label usernameLabel;

    public MainController(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    public void initialize() {
        usernameLabel.setText(mainApp.getLoggedInEmployee().getUsername());
    }

    @FXML
    public void handleLogout() {
        System.out.println("[*] Logged out");
        mainApp.setLoggedInEmployee(null);
        mainApp.loadPage("Login.fxml", new LoginController(mainApp), null);
    }

    @FXML
    public void showDashboard() {
        System.out.println("[+] Dashboard");
        mainApp.show("DashboardView.fxml", new DashboardController(mainApp));
    }

    @FXML
    public void showTicketsView() {
        System.out.println("[+] Tickets");
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }

    @FXML
    public void showAddTicket() {
        System.out.println("[+] Add Ticket");
        mainApp.show("AddEditTicketView.fxml", new AddEditTicketController(mainApp));
    }

    @FXML
    public void showEmployeesView() {
        System.out.println("[+] Employees");
        mainApp.show("EmployeesView.fxml", new EmployeesViewController(mainApp));
    }

}
