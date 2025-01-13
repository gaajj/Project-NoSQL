package org.example.projectsigma6;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.projectsigma6.controllers.DashboardController;
import org.example.projectsigma6.controllers.LoginController;
import org.example.projectsigma6.controllers.MainController;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private Employee loggedInEmployee;
    private BorderPane mainLayout;

    @Override
    public void start(Stage primaryStage) {
        try {
            this.primaryStage = primaryStage;

            // Load initial login page
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login.fxml"));
            LoginController loginController = new LoginController(this);
            fxmlLoader.setController(loginController);

            Scene loginScene = new Scene(fxmlLoader.load());
            primaryStage.setScene(loginScene);
            primaryStage.setTitle("Login");
            primaryStage.show();
        } catch (Exception e) {
            System.err.println("Failed to start the User Interface: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void showMainView() {
        try {
            System.out.println("[+] Loading MainView");
            FXMLLoader loader= new FXMLLoader(getClass().getResource("MainView.fxml"));
            loader.setController(new MainController(this));
            mainLayout = loader.load();

            Scene mainScene = new Scene(mainLayout);
            primaryStage.setScene(mainScene);
            primaryStage.setTitle("Ticket Management System");
            primaryStage.show();

            show("DashboardView.fxml", new DashboardController(this));
        } catch (Exception e) {
            System.err.println("Failed to load MainView: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void show(String fxmlFile, Object controller) {
        try {
            System.out.println("[+] Loading " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(controller);
            Node pageContent = loader.load();

            if (mainLayout != null) {
                mainLayout.setCenter(pageContent);
            }
        } catch (Exception e) {
            System.err.println("Failed to load '" + fxmlFile + "': " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadPage(String fxmlFile, Object controller, Pane contentArea) {
        try {
            System.out.println("[+] Loading " + fxmlFile);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(controller);

            if (contentArea != null) {
                Node view = loader.load();
                contentArea.getChildren().clear();
                contentArea.getChildren().add(view);
            } else {
                Scene scene = new Scene(loader.load());
                primaryStage.setScene(scene);
                primaryStage.show();
            }
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
