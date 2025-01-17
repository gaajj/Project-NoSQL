package org.example.projectsigma6.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.security.Provider;
import java.time.ZoneId;
import java.util.Date;

public class TicketViewController {

    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField priorityField;
    @FXML
    private TextField createdByField;
    @FXML
    private ComboBox<Employee> assignedToComboBox;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private ComboBox<TicketStatus> statusComboBox;
    @FXML
    private Label errorLabel;

    private Ticket ticket;
    private MainApp mainApp;
    private TicketService ticketService;
    private EmployeeService employeeService;

    public TicketViewController(MainApp mainApp, Ticket ticket) {
        this.ticket = ticket;
        this.mainApp = mainApp;
        this.ticketService = ServiceManager.getInstance().getTicketService();
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    @FXML
    private void initialize() {
        titleField.setText(ticket.getTitle());
        descriptionField.setText(ticket.getDescription());
        typeField.setText(ticket.getType().toString());
        priorityField.setText(ticket.getPriority().toString());
        createdByField.setText(ticket.getCreatedBy().getFullName());

        assignedToComboBox.setItems(FXCollections.observableArrayList(employeeService.getAllEmployees()));
        statusComboBox.setItems(FXCollections.observableArrayList(TicketStatus.values()));

        statusComboBox.setConverter(new StringConverter<TicketStatus>() {
            @Override
            public String toString(TicketStatus status) {
                return status != null ? status.toString() : "";
            }

            @Override
            public TicketStatus fromString(String string) {
                return string != null ? TicketStatus.valueOf(string) : null;
            }
        });

        assignedToComboBox.setValue(ticket.getAssignedTo());
        dueDatePicker.setValue(ticket.getDueDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        statusComboBox.setValue(ticket.getStatus());

        titleField.setEditable(false);
        descriptionField.setEditable(false);
        typeField.setEditable(false);
        priorityField.setEditable(false);
        createdByField.setEditable(false);

        assignedToComboBox.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee != null ? employee.getFullName() : "";
            }

            @Override
            public Employee fromString(String string) {
                return null;
            }
        });
    }

    @FXML
    private void handleSave() {
        if (assignedToComboBox.getValue() == null || statusComboBox.getValue() == null || dueDatePicker.getValue() == null) {
            errorLabel.setText("Please fill in all required fields.");
            errorLabel.setVisible(true);
            return;
        }

        ticket.setAssignedTo(assignedToComboBox.getValue());
        ticket.setStatus(TicketStatus.valueOf(statusComboBox.getValue().toString().toUpperCase()));
        ticket.setDueDate(Date.from(dueDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        ticketService.updateTicket(ticket);
        close();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        mainApp.show("TicketsView.fxml", new TicketsViewController(mainApp));
    }
}
