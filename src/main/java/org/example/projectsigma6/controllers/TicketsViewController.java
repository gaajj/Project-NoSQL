package org.example.projectsigma6.controllers;

import com.sun.tools.javac.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class TicketsViewController {

    @FXML
    private TableView<Ticket> ticketTable;

    @FXML
    private TableColumn<Ticket, String> columnTitle;

    @FXML
    private TableColumn<Ticket, String> columnPriority;

    @FXML
    private TableColumn<Ticket, String> columnStatus;

    @FXML
    private TableColumn<Ticket, String> columnType;

    @FXML
    private TableColumn<Ticket, String> columnCreatedBy;

    @FXML
    private TableColumn<Ticket, String> columnAssignedTo;

    @FXML
    private TableColumn<Ticket, String> columnDueDate;

    @FXML
    private Label totalTicketsLabel;

    @FXML
    private TextField searchField;

    private ObservableList<Ticket> ticketList;
    private MainApp mainApp;
    private final TicketService ticketService;
    private final TicketSearchController ticketSearchController;

    public TicketsViewController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.ticketService = ServiceManager.getInstance().getTicketService();
        this.ticketSearchController = new TicketSearchController();
    }

    @FXML
    public void initialize() {
        columnTitle.setCellValueFactory(param -> {
            String title = param.getValue().getTitle();
            return new SimpleStringProperty(title != null ? title : "No Title");
        });
        columnPriority.setCellValueFactory(param -> {
            TicketPriority priority = param.getValue().getPriority();
            return new SimpleStringProperty(priority != null ? priority.toString() : "N/A");
        });
        columnStatus.setCellValueFactory(param -> {
            TicketStatus status = param.getValue().getStatus();
            return new SimpleStringProperty(status != null ? status.toString() : "N/A");
        });
        columnType.setCellValueFactory(param -> {
            TicketType type = param.getValue().getType();
            return new SimpleStringProperty(type != null ? type.toString() : "N/A");
        });
        columnDueDate.setCellValueFactory(param -> {
            Date dueDate = param.getValue().getDueDate();
            return new SimpleStringProperty(dueDate != null ? dueDate.toString() : "N/A");
        });

        columnCreatedBy.setCellValueFactory(param -> {
            Employee createdBy = param.getValue().getCreatedBy();
            return new SimpleStringProperty(createdBy != null ? createdBy.getFullName() : "N/A");
        });

        columnAssignedTo.setCellValueFactory(param -> {
            Employee assignedTo = param.getValue().getAssignedTo();
            return new SimpleStringProperty(assignedTo != null ? assignedTo.getFullName() : "N/A");
        });

        // Add the double-click event listener to the ticketTable
        ticketTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // Check for double-click
                openDetailView();
            }
        });

        loadTickets();
    }

    private void openDetailView() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        mainApp.show("TicketView.fxml", new TicketViewController(mainApp, selectedTicket));
    }

    private void loadTickets() {
        // Fetch all tickets
        List<Ticket> tickets = ticketService.getAllTickets();
        ticketSearchController.setTickets(tickets);

        // Filter out deleted tickets
        List<Ticket> activeTickets = tickets.stream()
                .filter(ticket -> !ticket.isDeleted())
                .toList();

        // Set the filtered list to the table
        ticketList = FXCollections.observableArrayList(activeTickets);
        ticketTable.setItems(ticketList);

        // Update ticket count
        updateTotalTicketsLabel();
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        String searchLogic = "AND";

        List<Ticket> filteredTickets = ticketSearchController.searchTickets(searchText, searchLogic);

        ticketTable.setItems(FXCollections.observableArrayList(filteredTickets));
        updateTotalTicketsLabel();
    }

    private void updateTotalTicketsLabel() {
        totalTicketsLabel.setText(String.valueOf(ticketList.size()));
    }

    @FXML
    private void handleAddTicket() {
        mainApp.show("AddEditTicketView.fxml", new AddEditTicketController(mainApp));
    }

    @FXML
    private void handleEditTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            mainApp.show("AddEditTicketView.fxml", new AddEditTicketController(mainApp, selectedTicket));
        } else {
            showAlert("No Ticket Selected", "Please select a ticket to view/edit.");
        }
    }

    @FXML
    private void handleDeleteTicket() {
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            ticketService.removeTicket(selectedTicket);
            ticketList.remove(selectedTicket);
            updateTotalTicketsLabel();
        } else {
            showAlert("No Ticket Selected", "Please select a ticket to delete.");
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
