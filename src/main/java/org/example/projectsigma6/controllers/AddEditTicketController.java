package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.bson.types.ObjectId;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class AddEditTicketController {

    private MainApp mainApp;
    private TicketService ticketService;
    private Ticket ticket; // The ticket being edited (or null for new ticket)

    // FXML fields for the view
    @FXML private TextField titleField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<TicketType> typeComboBox;
    @FXML private ComboBox<TicketPriority> priorityComboBox;
    @FXML private DatePicker dueDatePicker;
    @FXML private ComboBox<Employee> assignedToComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Constructor for adding a new ticket
    public AddEditTicketController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.ticketService = ServiceManager.getInstance().getTicketService();
    }

    // Constructor for editing an existing ticket
    public AddEditTicketController(MainApp mainApp, Ticket ticket) {
        this.mainApp = mainApp;
        this.ticketService = ServiceManager.getInstance().getTicketService();
        this.ticket = ticket;
    }

    @FXML
    public void initialize() {
        // Initialize ComboBoxes with enum values
        typeComboBox.getItems().setAll(TicketType.values());
        priorityComboBox.getItems().setAll(TicketPriority.values());

        // Populate the ComboBox with employees for "assignedTo"
        // Assume that EmployeeService gets all employees from the database or a service
        assignedToComboBox.getItems().setAll(ServiceManager.getInstance().getEmployeeService().getAllEmployees());

        if (ticket != null) {
            // If editing an existing ticket, populate fields with ticket data
            titleField.setText(ticket.getTitle());
            descriptionArea.setText(ticket.getDescription());
            typeComboBox.setValue(ticket.getType());
            priorityComboBox.setValue(ticket.getPriority());
            dueDatePicker.setValue(ticket.getDueDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate());
            assignedToComboBox.setValue(ticket.getAssignedTo());
        }

        // Save button action
        saveButton.setOnAction(event -> saveTicket());

        // Cancel button action
        cancelButton.setOnAction(event -> cancelEdit());
    }

    public Date getDateFromDatePicker() {
        LocalDate localDate = dueDatePicker.getValue(); // Get the selected date
        if (localDate != null) {
            // Convert LocalDate to java.util.Date
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null; // Handle the case where no date is selected
    }

    private void saveTicket() {
        // Collect all the data from the fields
        String title = titleField.getText();
        String description = descriptionArea.getText();
        TicketType type = typeComboBox.getValue();
        TicketPriority priority = priorityComboBox.getValue();
        Date dueDate = getDateFromDatePicker();
        Employee assignedTo = assignedToComboBox.getValue();

        if (ticket == null) {
            // If the ticket is null, create a new ticket
            Ticket newTicket = new Ticket();
            newTicket.setId(new ObjectId());
            newTicket.setTitle(title);
            newTicket.setDescription(description);
            newTicket.setType(type);
            newTicket.setStatus(TicketStatus.OPEN);
            newTicket.setPriority(priority);
            newTicket.setDueDate(dueDate);
            newTicket.setAssignedTo(assignedTo);
            newTicket.setCreatedAt(new Date());
            newTicket.setCreatedBy(mainApp.getLoggedInEmployee());

            // Save the new ticket using the ticketService
            ticketService.addTicket(newTicket);
        } else {
            System.out.println(ticket.toString());
            // If editing an existing ticket, update the ticket
            ticket.setTitle(title);
            ticket.setDescription(description);
            ticket.setType(type);
            ticket.setPriority(priority);
            ticket.setDueDate(dueDate);
            ticket.setAssignedTo(assignedTo);

            System.out.println(ticket.toString());
            // Save the edited ticket using the ticketService
            ticketService.updateTicket(ticket);
        }

        // After saving, go back to the main view (dashboard or ticket list)
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }

    private void cancelEdit() {
        // Go back to the main view without saving
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }
}
