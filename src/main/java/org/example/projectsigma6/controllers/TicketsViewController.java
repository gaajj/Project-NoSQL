package org.example.projectsigma6.controllers;

import com.sun.tools.javac.Main;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

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

    private ObservableList<Ticket> ticketList;
    private MainApp mainApp;
    private final TicketService ticketService;

    public TicketsViewController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.ticketService = ServiceManager.getInstance().getTicketService();
    }

    @FXML
    public void initialize() {
        // Configure cell value factories for simple fields
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

        // Configure custom cell value factories for Employee fields
        columnCreatedBy.setCellValueFactory(param -> {
            Employee createdBy = param.getValue().getCreatedBy();
            return new SimpleStringProperty(createdBy != null ? createdBy.getFullName() : "N/A");
        });

        columnAssignedTo.setCellValueFactory(param -> {
            Employee assignedTo = param.getValue().getAssignedTo();
            return new SimpleStringProperty(assignedTo != null ? assignedTo.getFullName() : "N/A");
        });

        // Load data into the table
        loadTickets();
    }

    private void loadTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        ticketList = FXCollections.observableArrayList(tickets);
        ticketTable.setItems(ticketList);
        updateTotalTicketsLabel();
    }

    private void updateTotalTicketsLabel() {
        totalTicketsLabel.setText(String.valueOf(ticketList.size()));
    }

    @FXML
    private void handleAddTicket() {
        // Handle logic to add a ticket
        System.out.println("[+] Add Ticket button clicked.");
        // Implement navigation to ticket creation form
    }

    @FXML
    private void handleEditTicket() {
        // Handle logic to edit a ticket
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            System.out.println("[+] Edit Ticket: " + selectedTicket.getTitle());
            // Implement navigation to ticket editing form
        } else {
            System.out.println("[!] No ticket selected for editing.");
        }
    }

    @FXML
    private void handleDeleteTicket() {
        // Handle logic to delete a ticket
        Ticket selectedTicket = ticketTable.getSelectionModel().getSelectedItem();
        if (selectedTicket != null) {
            System.out.println("[+] Deleting Ticket: " + selectedTicket.getTitle());

        } else {
            System.out.println("[!] No ticket selected for deletion.");
        }
    }
}
