package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.EmployeeType;
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
    private Ticket ticket;

    @FXML private Label addEditLabel;
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
        // Populate the type and priority ComboBoxes
        typeComboBox.getItems().setAll(TicketType.values());
        priorityComboBox.getItems().setAll(TicketPriority.values());

        // Set the default priority to LOW
        priorityComboBox.setValue(TicketPriority.LOW);

        // Retrieve and sort employees by type, prioritizing SERVICEDESK
        var employees = ServiceManager.getInstance().getEmployeeService().getAllEmployees();
        employees.sort((e1, e2) -> {
            if (e1.getEmployeeType() == EmployeeType.SERVICEDESK && e2.getEmployeeType() != EmployeeType.SERVICEDESK) {
                return -1;
            } else if (e1.getEmployeeType() != EmployeeType.SERVICEDESK && e2.getEmployeeType() == EmployeeType.SERVICEDESK) {
                return 1;
            } else {
                return e1.getFullName().compareToIgnoreCase(e2.getFullName());
            }
        });

        // Populate the assignedToComboBox with sorted employees
        assignedToComboBox.getItems().setAll(employees);

        // Configure how employees are displayed in the ComboBox
        assignedToComboBox.setCellFactory(comboBox -> new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(String.format("%s, %s, %s",
                            employee.getFullName(),
                            employee.getId().toHexString(),
                            employee.getEmployeeType()));
                }
            }
        });

        assignedToComboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Employee employee, boolean empty) {
                super.updateItem(employee, empty);
                if (empty || employee == null) {
                    setText(null);
                } else {
                    setText(String.format("%s, %s, %s",
                            employee.getFullName(),
                            employee.getId().toHexString(),
                            employee.getEmployeeType()));
                }
            }
        });

        // If editing an existing ticket, populate fields with ticket data
        if (ticket != null) {
            titleField.setText(ticket.getTitle());
            descriptionArea.setText(ticket.getDescription());
            typeComboBox.setValue(ticket.getType());
            priorityComboBox.setValue(ticket.getPriority());
            dueDatePicker.setValue(ticket.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            // Match the assignedToComboBox value with the existing ticket's assignedTo
            Employee assignedEmployee = ticket.getAssignedTo();
            if (assignedEmployee != null) {
                assignedToComboBox.setValue(employees.stream()
                        .filter(emp -> emp.getId().equals(assignedEmployee.getId()))
                        .findFirst()
                        .orElse(null));
            } else {
                // If no employee is assigned, set it to null
                assignedToComboBox.setValue(null);
            }

            addEditLabel.setText("Edit Ticket");
        } else {
            // Set default values for a new ticket
            addEditLabel.setText("Add Ticket");
            dueDatePicker.setValue(LocalDate.now());
        }

        // Configure save and cancel button actions
        saveButton.setOnAction(event -> saveTicket());
        cancelButton.setOnAction(event -> cancelEdit());

        // When a new assignment is selected, save the change
        assignedToComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (ticket != null) {
                ticket.setAssignedTo(newValue);  // Set the new employee or null
            }
        });
    }

    public Date getDateFromDatePicker() {
        LocalDate localDate = dueDatePicker.getValue();
        if (localDate != null) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    public void saveTicket() {
        // Ensure the title and description fields are not empty
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TicketType type = typeComboBox.getValue();
        TicketPriority priority = priorityComboBox.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        // Validate fields
        if (title.isEmpty() || description.isEmpty() || type == null || priority == null || dueDate == null) {
            // Show error message (you can use a dialog or status label)
            showError("All fields are required!");
            return;
        }

        // Create a new ticket if we're adding a new one, or update the existing one
        if (ticket == null) {
            // Create a new ticket
            ticket = new Ticket();
        }

        // Update ticket details
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setType(type);
        ticket.setPriority(priority);
        ticket.setDueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        // Set assigned employee if one is selected, or null if none is selected
        if (assignedToComboBox.getValue() != null) {
            ticket.setAssignedTo(assignedToComboBox.getValue());
        } else {
            ticket.setAssignedTo(null);  // No employee assigned
        }

        // If the ticket is new, save it to the database or wherever it's being stored
        if (ticket.getId() == null) {
            // Save the new ticket (you can call the service to persist it)
            ServiceManager.getInstance().getTicketService().addTicket(ticket);
        } else {
            // Update the existing ticket (assuming the ticket has an ID)
            ServiceManager.getInstance().getTicketService().updateTicket(ticket);
        }

        // Optionally, you can display a success message or close the current view
        showSuccess("Ticket saved successfully!");
        close();  // Close the current view after saving
    }

    private void showError(String message) {
        // Show error message (you can implement this however you'd like, e.g., using an alert or a label)
        System.out.println("Error: " + message); // Replace with a proper UI message display
    }

    private void showSuccess(String message) {
        // Show success message (you can implement this however you'd like)
        System.out.println("Success: " + message); // Replace with a proper UI message display
    }

    private void cancelEdit() {
        close();
    }

    private void close() {
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }
}

