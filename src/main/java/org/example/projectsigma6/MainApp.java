package org.example.projectsigma6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.projectsigma6.controllers.LoginController;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.io.IOException;

public class MainApp extends Application {

    private EmployeeService employeeService;
    private TicketService ticketService;
    private Employee loggedInEmployee;

    @Override
    public void start(Stage primaryStage) {
        try {
            employeeService = ServiceManager.getInstance().getEmployeeService();
            ticketService = ServiceManager.getInstance().getTicketService();

            loadPage(primaryStage, "Login.fxml", new LoginController(this), "Login");
        } catch (Exception e) {
            System.err.println("Failed to start the User Interface: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadPage(Stage primaryStage, String fxmlFile, Object Controller, String title) {
        try {
            System.out.println("[+] Loading " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(Controller);
            Scene scene = new Scene(loader.load());

            primaryStage.setScene(scene);
            primaryStage.setTitle(title);
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load page '" + fxmlFile + "': " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void setLoggedInEmployee(Employee employee) {
        this.loggedInEmployee = employee;
    }

    public Employee getLoggedInEmployee() {
        return loggedInEmployee;
    }

    public static void main(String[] args) {
        launch();
    }

}
