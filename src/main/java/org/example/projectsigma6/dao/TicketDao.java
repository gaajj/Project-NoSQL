package org.example.projectsigma6.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.scene.Parent;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketDao extends BaseDao<Ticket> {

    public TicketDao(MongoClient mongoClient, String databaseName, String collectionName) {
        super(mongoClient, databaseName, collectionName, Ticket.class);
    }

    public List<Ticket> getAllTickets() {
        try {
            List<Ticket> tickets = collection.find().into(new ArrayList<>());
            System.out.println("Employees tickets successfully");
            return tickets;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket getTicketById(String id) {
        try {
            Ticket ticket = collection.find(Filters.eq("_id", id)).first();
            System.out.println("Ticket retrieved successfully: " + ticket.toStringShort());
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket addTicket(Ticket ticket) {
        try {
            collection.insertOne(ticket);
            System.out.println("Ticket added successfully: " + ticket.toStringShort());
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao adding employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket removeTicket(Ticket ticket) {
        try {
            collection.updateOne(
                    Filters.eq("_id", ticket.getId().toString()),
                    Updates.set("inEmployment", false)
            );
            System.out.println("Ticket removed successfully: " + ticket.toStringShort());
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in EmployeeDao removing employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
