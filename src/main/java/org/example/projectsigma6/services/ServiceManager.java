package org.example.projectsigma6.services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.example.projectsigma6.config.Config;

public class ServiceManager {

    private static ServiceManager instance;
    private EmployeeService employeeService;
    private TicketService ticketService;

    private ServiceManager() {
        try {
            String uri = Config.getMongoUri();
            MongoClient mongoClient = MongoClients.create(uri);

            this.employeeService = new EmployeeService(mongoClient, "ProjectSigma");
            this.ticketService = new TicketService(mongoClient, "ProjectSigma");
        } catch (Exception e) {
            System.err.println("Failed to initialize MongoDB connection: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static ServiceManager getInstance() {
        if (instance == null) {
            instance = new ServiceManager();
        }
        return instance;
    }

    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    public TicketService getTicketService() {
        return ticketService;
    }

}
