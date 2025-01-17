package org.example.projectsigma6.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.example.projectsigma6.MainApp;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.enums.EmployeeType;
import org.example.projectsigma6.models.enums.Location;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

public class AddEditEmployeeController {

    private MainApp mainApp;
    private EmployeeService employeeService;
    private Employee employee;

    // FXML fields for the view
    @FXML private Label addEditLabel;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;  // Password field
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private ComboBox<EmployeeType> employeeTypeComboBox;
    @FXML private ComboBox<Location> locationComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Label errorLabel;

    @FXML private Label passwordExplanationLabel;

    // Constructor for adding a new employee
    public AddEditEmployeeController(MainApp mainApp) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
    }

    // Constructor for editing an existing employee
    public AddEditEmployeeController(MainApp mainApp, Employee employee) {
        this.mainApp = mainApp;
        this.employeeService = ServiceManager.getInstance().getEmployeeService();
        this.employee = employee;
    }

    @FXML
    public void initialize() {

        employeeTypeComboBox.getItems().setAll(EmployeeType.values());
        locationComboBox.getItems().setAll(Location.values());

        if (employee != null) {
            usernameField.setText(employee.getUsername());
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            emailField.setText(employee.getEmail());
            phoneField.setText(employee.getPhoneNumber());
            employeeTypeComboBox.setValue(employee.getEmployeeType());
            locationComboBox.setValue(employee.getLocation());

            addEditLabel.setText("Edit Employee");

            passwordExplanationLabel.setVisible(true);
        } else {
            addEditLabel.setText("Add Employee");

            passwordExplanationLabel.setVisible(false);
        }

        saveButton.setOnAction(event -> saveEmployee());
        cancelButton.setOnAction(event -> cancelEdit());
    }

    public void saveEmployee() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        EmployeeType employeeType = employeeTypeComboBox.getValue();
        Location location = locationComboBox.getValue();

        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || employeeType == null || location == null || (employee == null && password.isEmpty())) {
            errorLabel.setText("All fields are required!");
            errorLabel.setVisible(true);
            return;
        } else {
            errorLabel.setVisible(false);
        }

        if (employee != null) {
            employee.setUsername(username);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setPhoneNumber(phone);
            employee.setEmployeeType(employeeType);
            employee.setLocation(location);

            if (!password.isEmpty()) {
                String salt = employeeService.generateSalt();
                String hashedPassword = employeeService.hashPassword(password, salt);
                employee.setHashedPassword(hashedPassword);
            }

            employeeService.updateEmployee(employee);

        } else {
            Employee newEmployee = new Employee();
            newEmployee.setId(new ObjectId());
            newEmployee.setUsername(username);
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setEmail(email);
            newEmployee.setPhoneNumber(phone);
            newEmployee.setEmployeeType(employeeType);
            newEmployee.setLocation(location);
            newEmployee.setInEmployment(true);

            String salt = employeeService.generateSalt();
            String hashedPassword = employeeService.hashPassword(password, salt);
            newEmployee.setHashedPassword(hashedPassword);
            newEmployee.setSalt(salt);

            employeeService.addEmployee(newEmployee);
        }

        close();
    }

    private void cancelEdit() {
        close();
    }

    private void close() {
        mainApp.show("EmployeesView.fxml", new EmployeesViewController(mainApp));
    }
}
