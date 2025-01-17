package org.example.projectsigma6.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TicketsViewController {

    private Employee loggedInEmployee;

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

    @FXML
    private Button editTicketButton;
    @FXML
    private Button deleteTicketButton;

    @FXML
    private ComboBox<TicketPriority> priorityComboBox;

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
        this.loggedInEmployee = mainApp.getLoggedInEmployee();
        if (loggedInEmployee.getEmployeeType() == EmployeeType.REGULAR) {
            editTicketButton.setVisible(false);
            deleteTicketButton.setVisible(false);
        }

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
        // Initialize columns
        columnTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        columnPriority.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPriority() != null ? param.getValue().getPriority().toString() : "N/A"));
        columnStatus.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus() != null ? param.getValue().getStatus().toString() : "N/A"));
        columnType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType() != null ? param.getValue().getType().toString() : "N/A"));
        columnDueDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDueDate() != null ? param.getValue().getDueDate().toString() : "N/A"));
        columnCreatedBy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCreatedBy() != null ? param.getValue().getCreatedBy().getFullName() : "N/A"));
        columnAssignedTo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAssignedTo() != null ? param.getValue().getAssignedTo().getFullName() : "N/A"));

        ObservableList<TicketPriority> priorities = FXCollections.observableArrayList(TicketPriority.values());
        priorities.add(0, null);
        priorityComboBox.setItems(priorities);

        priorityComboBox.getSelectionModel().selectFirst();

        priorityComboBox.setOnAction(event -> handleFilterPriority());

        ticketTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
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
        List<Ticket> tickets = new ArrayList<>();
        if (loggedInEmployee.getEmployeeType() == EmployeeType.REGULAR) {
            tickets = ServiceManager.getInstance().getTicketService().getTicketsByEmployee(loggedInEmployee);
        } else {
            tickets = ServiceManager.getInstance().getTicketService().getAllTickets();
        }
        ticketSearchController.setTickets(tickets);

        List<Ticket> activeTickets = tickets.stream()
                .filter(ticket -> !ticket.isDeleted())
                .collect(Collectors.toList());

        ticketList = FXCollections.observableArrayList(activeTickets);
        ticketTable.setItems(ticketList);

        updateTotalTicketsLabel();
    }

    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().trim();
        String searchLogic = "AND";

        List<Ticket> filteredTickets = ticketSearchController.searchTickets(searchText, searchLogic);

        TicketPriority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();
        if (selectedPriority != null) {
            filteredTickets = filteredTickets.stream()
                    .filter(ticket -> ticket.getPriority() == selectedPriority)
                    .collect(Collectors.toList());
        }

        ticketTable.setItems(FXCollections.observableArrayList(filteredTickets));
        updateTotalTicketsLabel();
    }

    @FXML
    private void handleFilterPriority() {
        String searchText = searchField.getText().trim();
        String searchLogic = "AND";

        List<Ticket> filteredTickets = ticketSearchController.searchTickets(searchText, searchLogic);

        TicketPriority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();

        if (selectedPriority != null) {
            filteredTickets = filteredTickets.stream()
                    .filter(ticket -> ticket.getPriority() == selectedPriority)
                    .collect(Collectors.toList());
        }

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
