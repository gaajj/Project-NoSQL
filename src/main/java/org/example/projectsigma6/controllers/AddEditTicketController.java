package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
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
            Employee assignedEmployee = employees.stream()
                    .filter(emp -> emp.getId().equals(ticket.getAssignedTo().getId()))
                    .findFirst()
                    .orElse(null);

            assignedToComboBox.setValue(assignedEmployee);
            addEditLabel.setText("Edit Ticket");
        } else {
            // Set default values for a new ticket
            addEditLabel.setText("Add Ticket");
            dueDatePicker.setValue(LocalDate.now());
        }

        // Configure save and cancel button actions
        saveButton.setOnAction(event -> saveTicket());
        cancelButton.setOnAction(event -> cancelEdit());
    }



    public Date getDateFromDatePicker() {
        LocalDate localDate = dueDatePicker.getValue();
        if (localDate != null) {
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    private void saveTicket() {
        // Collect input values
        String title = titleField.getText();
        String description = descriptionArea.getText();
        TicketType type = typeComboBox.getValue();
        TicketPriority priority = priorityComboBox.getValue();
        Date dueDate = getDateFromDatePicker();
        Employee assignedTo = assignedToComboBox.getValue();

        // Validation: Mandatory fields except assignedTo
        if (title == null || title.isBlank()) {
            showError("Title is mandatory.");
            return;
        }
        if (description == null || description.isBlank()) {
            showError("Description is mandatory.");
            return;
        }
        if (type == null) {
            showError("Ticket type is mandatory.");
            return;
        }
        if (priority == null) {
            showError("Ticket priority is mandatory.");
            return;
        }
        if (dueDate == null) {
            showError("Due date is mandatory.");
            return;
        }

        if (ticket == null) {
            // Creating a new ticket
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

            ticketService.addTicket(newTicket);
        } else {
            // Updating an existing ticket
            ticket.setTitle(title);
            ticket.setDescription(description);
            ticket.setType(type);
            ticket.setPriority(priority);
            ticket.setDueDate(dueDate);
            ticket.setAssignedTo(assignedTo);

            ticketService.updateTicket(ticket);
        }

        close();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void cancelEdit() {
        close();
    }

    private void close() {
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }
}
