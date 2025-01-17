package org.example.projectsigma6.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.scene.Parent;
import org.bson.conversions.Bson;
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
            System.out.println("Employees tickets successfully retrieved");
            return tickets;
        } catch (Exception e) {
            System.err.println("Error in TicketDao retrieving employee by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public List<Ticket> getTicketsByEmployee(Employee employee) {
        try {
            List<Ticket> tickets = collection.find(Filters.eq("createdBy._id", employee.getId().toString())).into(new ArrayList<>());
            for (Ticket ticket : tickets) {
                ticket.toStringShort();
            }
            System.out.println("Employees tickets successfully retrieved");
            return tickets;
        } catch (Exception e) {
            System.err.println("Error in TicketDao retrieving tickets from employee: " + e.getMessage());
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
            System.err.println("Error in TicketDao retrieving ticket by ID: " + e.getMessage());
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
            System.err.println("Error in TicketDao adding ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket removeTicket(Ticket ticket) {
        try {
            collection.updateOne(
                    Filters.eq("_id", ticket.getId().toString()),
                    Updates.set("isDeleted", true)
            );
            System.out.println("Ticket removed successfully: " + ticket.toStringShort());
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in TicketDao removing ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket updateTicket(Ticket ticket) {
        try {
            Ticket existingTicket = getTicketById(ticket.getId().toString());
            List<Bson> updates = new ArrayList<>();

            if (!existingTicket.getTitle().equals(ticket.getTitle())) {
                updates.add(Updates.set("title", ticket.getTitle()));
            }
            if (!existingTicket.getDescription().equals(ticket.getDescription())) {
                updates.add(Updates.set("description", ticket.getDescription()));
            }
            if (!existingTicket.getType().equals(ticket.getType())) {
                updates.add(Updates.set("type", ticket.getType()));
            }
            if (!existingTicket.getStatus().equals(ticket.getStatus())) {
                updates.add(Updates.set("status", ticket.getStatus()));
            }
            if (!existingTicket.getPriority().equals(ticket.getPriority())) {
                updates.add(Updates.set("priority", ticket.getPriority()));
            }
            if (!existingTicket.getCreatedBy().equals(ticket.getCreatedBy())) {
                updates.add(Updates.set("createdBy", ticket.getCreatedBy()));
            }
            if (existingTicket.getAssignedTo() != null) {
                if (!existingTicket.getAssignedTo().equals(ticket.getAssignedTo())) {
                    updates.add(Updates.set("assignedTo", ticket.getAssignedTo()));
                }
            } else {
                updates.add(Updates.set("assignedTo", null));
            }
            if (!existingTicket.getCreatedAt().equals(ticket.getCreatedAt())) {
                updates.add(Updates.set("createdAt", ticket.getCreatedAt()));
            }
            if (!existingTicket.getDueDate().equals(ticket.getDueDate())) {
                updates.add(Updates.set("dueDate", ticket.getDueDate()));
            }
            if (existingTicket.isDeleted() == ticket.isDeleted()) {
                updates.add(Updates.set("isDeleted", ticket.isDeleted()));
            }

            if (!updates.isEmpty()) {
                collection.updateOne(
                        Filters.eq("_id", ticket.getId().toString()),
                        Updates.combine(updates)
                );
            }
            System.out.println("Ticket updated successfully: " + ticket.toStringShort());
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in TicketDao updating ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
