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
        typeComboBox.getItems().setAll(TicketType.values());
        priorityComboBox.getItems().setAll(TicketPriority.values());

        priorityComboBox.setValue(TicketPriority.LOW);

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
                assignedToComboBox.setValue(null);
            }

            addEditLabel.setText("Edit Ticket");
        } else {
            addEditLabel.setText("Add Ticket");
            dueDatePicker.setValue(LocalDate.now());
        }

        saveButton.setOnAction(event -> saveTicket());
        cancelButton.setOnAction(event -> cancelEdit());

        assignedToComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (ticket != null) {
                ticket.setAssignedTo(newValue);
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
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        TicketType type = typeComboBox.getValue();
        TicketPriority priority = priorityComboBox.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (title.isEmpty() || description.isEmpty() || type == null || priority == null || dueDate == null) {
            showError("All fields are required!");
            return;
        }
        if (ticket == null) {
            ticket = new Ticket();
        }

        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setType(type);
        ticket.setPriority(priority);
        ticket.setDueDate(Date.from(dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));

        if (assignedToComboBox.getValue() != null) {
            ticket.setAssignedTo(assignedToComboBox.getValue());
        } else {
            ticket.setAssignedTo(null);
        }

        if (ticket.getId() == null) {
            ServiceManager.getInstance().getTicketService().addTicket(ticket);
        } else {
            ServiceManager.getInstance().getTicketService().updateTicket(ticket);
        }

        showSuccess("Ticket saved successfully!");
        close();
    }

    private void showError(String message) {
        System.out.println("Error: " + message);
    }

    private void showSuccess(String message) {
        System.out.println("Success: " + message);
    }

    private void cancelEdit() {
        close();
    }

    private void close() {
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }
}

