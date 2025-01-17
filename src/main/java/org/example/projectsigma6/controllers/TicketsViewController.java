package org.example.projectsigma6.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.util.stream.Collectors;

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

    @FXML
    private ComboBox<TicketPriority> priorityComboBox;  // ComboBox for priority filter

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
        // Initialize columns
        columnTitle.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getTitle()));
        columnPriority.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getPriority() != null ? param.getValue().getPriority().toString() : "N/A"));
        columnStatus.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStatus() != null ? param.getValue().getStatus().toString() : "N/A"));
        columnType.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getType() != null ? param.getValue().getType().toString() : "N/A"));
        columnDueDate.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDueDate() != null ? param.getValue().getDueDate().toString() : "N/A"));
        columnCreatedBy.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCreatedBy() != null ? param.getValue().getCreatedBy().getFullName() : "N/A"));
        columnAssignedTo.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getAssignedTo() != null ? param.getValue().getAssignedTo().getFullName() : "N/A"));

        // Initialize the priority ComboBox with "Show All" option
        ObservableList<TicketPriority> priorities = FXCollections.observableArrayList(TicketPriority.values());
        priorities.add(0, null); // Add null at the start to represent "Show All"
        priorityComboBox.setItems(priorities);

        // Set default selection to "Show All" (null)
        priorityComboBox.getSelectionModel().selectFirst();

        // Set an event listener for ComboBox changes
        priorityComboBox.setOnAction(event -> handleFilterPriority());

        // Add the double-click event listener to the ticketTable
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
        // Fetch all tickets
        List<Ticket> tickets = ticketService.getAllTickets();
        ticketSearchController.setTickets(tickets);

        // Filter out deleted tickets
        List<Ticket> activeTickets = tickets.stream()
                .filter(ticket -> !ticket.isDeleted())
                .collect(Collectors.toList());

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

        // Apply search logic
        List<Ticket> filteredTickets = ticketSearchController.searchTickets(searchText, searchLogic);

        // Apply priority filtering if a priority is selected
        TicketPriority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();
        filteredTickets = filteredTickets.stream()
                .filter(ticket -> ticket.getPriority() == selectedPriority)
                .collect(Collectors.toList());

        ticketTable.setItems(FXCollections.observableArrayList(filteredTickets));
        updateTotalTicketsLabel();
    }

    @FXML
    private void handleFilterPriority() {
        String searchText = searchField.getText().trim();
        String searchLogic = "AND";

        // Apply search logic
        List<Ticket> filteredTickets = ticketSearchController.searchTickets(searchText, searchLogic);

        // Get the selected priority from the ComboBox
        TicketPriority selectedPriority = priorityComboBox.getSelectionModel().getSelectedItem();

        if (selectedPriority != null) {
            // If a priority is selected, filter by that priority
            filteredTickets = filteredTickets.stream()
                    .filter(ticket -> ticket.getPriority() == selectedPriority)
                    .collect(Collectors.toList());
        }

        // Update the table view with the filtered list
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
