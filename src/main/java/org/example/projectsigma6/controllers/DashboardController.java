package org.example.projectsigma6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.services.ServiceManager;

import java.util.List;

public class DashboardController {

    private MainApp mainApp;
    private Employee loggedInEmployee;

    private int openTickets;
    private int closedTickets;
    private int inProgressTickets;

    @FXML
    private PieChart ticketStatsPieChart;

    public DashboardController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.loggedInEmployee = mainApp.getLoggedInEmployee();
    }

    @FXML
    public void initialize() {
        updateTicketStats();
    }

    public void updateTicketStats() {
        getTicketData();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Open", openTickets),
                new PieChart.Data("Closed", closedTickets),
                new PieChart.Data("In Progress", inProgressTickets)
        );
        ticketStatsPieChart.setData(pieChartData);
    }

    public void getTicketData() {
        List<Ticket> Tickets = ServiceManager.getInstance().getTicketService().getAllTickets();

        for (Ticket ticket : Tickets) {
            switch (ticket.getStatus()) {
                case TicketStatus.OPEN -> openTickets++;
                case TicketStatus.CLOSED -> closedTickets++;
                case TicketStatus.IN_PROGRESS -> inProgressTickets++;
            }
        }
    }

    @FXML
    public void handleLogout() {
        mainApp.setLoggedInEmployee(null);
        mainApp.loadPage("Login.fxml", new LoginController(mainApp), null);
    }

}
