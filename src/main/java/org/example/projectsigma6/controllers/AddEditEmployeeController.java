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
        // Populate the employee type and location ComboBoxes
        employeeTypeComboBox.getItems().setAll(EmployeeType.values());
        locationComboBox.getItems().setAll(Location.values());

        // If editing an existing employee, populate fields with employee data
        if (employee != null) {
            usernameField.setText(employee.getUsername());
            firstNameField.setText(employee.getFirstName());
            lastNameField.setText(employee.getLastName());
            emailField.setText(employee.getEmail());
            phoneField.setText(employee.getPhoneNumber());
            employeeTypeComboBox.setValue(employee.getEmployeeType());
            locationComboBox.setValue(employee.getLocation());

            addEditLabel.setText("Edit Employee");

            // Show password explanation for editing
            passwordExplanationLabel.setVisible(true);
        } else {
            // Set default values for a new employee
            addEditLabel.setText("Add Employee");

            // Hide the password explanation label for adding
            passwordExplanationLabel.setVisible(false);
        }

        // Configure save and cancel button actions
        saveButton.setOnAction(event -> saveEmployee());
        cancelButton.setOnAction(event -> cancelEdit());
    }

    public void saveEmployee() {
        // Ensure all fields are filled
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();  // Get the password input
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        EmployeeType employeeType = employeeTypeComboBox.getValue();
        Location location = locationComboBox.getValue();

        // Validate fields
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || employeeType == null || location == null || (employee == null && password.isEmpty())) {
            errorLabel.setText("All fields are required!");
            errorLabel.setVisible(true);
            return;
        } else {
            errorLabel.setVisible(false);
        }

        // If editing an existing employee
        if (employee != null) {
            employee.setUsername(username);
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setEmail(email);
            employee.setPhoneNumber(phone);
            employee.setEmployeeType(employeeType);
            employee.setLocation(location);

            // Only hash the password if it's changed
            if (!password.isEmpty()) {
                String salt = employeeService.generateSalt();
                String hashedPassword = employeeService.hashPassword(password, salt);
                employee.setHashedPassword(hashedPassword);
            }

            // Update the employee in the database
            employeeService.updateEmployee(employee);

        } else {
            // Create a new employee if none exists
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

            // Hash password for new employee
            String salt = employeeService.generateSalt();
            String hashedPassword = employeeService.hashPassword(password, salt);
            newEmployee.setHashedPassword(hashedPassword);
            newEmployee.setSalt(salt);

            // Add the new employee to the database
            employeeService.addEmployee(newEmployee);
        }

        close();  // Close the current view after saving
    }

    private void cancelEdit() {
        close();
    }

    private void close() {
        mainApp.show("EmployeesView.fxml", new EmployeesViewController(mainApp));
    }
}
