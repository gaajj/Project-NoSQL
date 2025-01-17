package org.example.projectsigma6.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.services.ServiceManager;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class DashboardController {

    private MainApp mainApp;
    private Employee loggedInEmployee;

    private int openTickets;
    private int closedTickets;
    private int inProgressTickets;
    private int overdueTickets;
    private int dueTodayTickets;
    private int notDueTickets;

    @FXML
    private Pane dashboardContent;
    @FXML
    private GridPane gridPane;
    @FXML
    private PieChart ticketStatsPieChart;
    @FXML
    private PieChart ticketDuePieChart;

    public DashboardController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.loggedInEmployee = mainApp.getLoggedInEmployee();
    }

    @FXML
    public void initialize() {
        gridPane.prefWidthProperty().bind(dashboardContent.widthProperty().subtract(40)); // Subtract padding
        gridPane.prefHeightProperty().bind(dashboardContent.heightProperty().subtract(40)); // Subtract padding

        updateTicketStats();
    }

    private void updateTicketStats() {
        getTicketData();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Open", openTickets),
                new PieChart.Data("Closed", closedTickets),
                new PieChart.Data("In Progress", inProgressTickets)
        );
        ticketStatsPieChart.setData(pieChartData);

        ObservableList<PieChart.Data> dueDatePieChartData= FXCollections.observableArrayList(
                new PieChart.Data("Overdue", overdueTickets),
                new PieChart.Data("Due Soon", dueTodayTickets),
                new PieChart.Data("Not Due", notDueTickets)
        );
        ticketDuePieChart.setData(dueDatePieChartData);
    }

    private void getTicketData() {
        List<Ticket> Tickets = ServiceManager.getInstance().getTicketService().getAllTickets();

        openTickets = 0;
        closedTickets = 0;
        inProgressTickets = 0;
        overdueTickets = 0;
        dueTodayTickets = 0;
        notDueTickets = 0;

        Date today = new Date();

        for (Ticket ticket : Tickets) {
            switch (ticket.getStatus()) {
                case TicketStatus.OPEN -> openTickets++;
                case TicketStatus.CLOSED -> closedTickets++;
                case TicketStatus.IN_PROGRESS -> inProgressTickets++;
            }
            Date dueDate = ticket.getDueDate();

            if (dueDate != null) {
                if (dueDate.before(today)) {
                    overdueTickets++;
                } else if (dueDate.equals(today)) {
                    dueTodayTickets++;
                } else {
                    notDueTickets++;
                }
            }
        }
    }

}
