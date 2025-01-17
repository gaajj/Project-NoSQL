package org.example.projectsigma6.services;

import com.mongodb.client.MongoClient;
import org.example.projectsigma6.dao.EmployeeDao;
import org.example.projectsigma6.dao.TicketDao;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;

import java.util.List;

public class TicketService {

    private final TicketDao ticketDao;

    public TicketService(MongoClient mongoClient, String databaseName) {
        this.ticketDao = new TicketDao(mongoClient, databaseName, "Ticket");
    }

    public List<Ticket> getAllTickets() {
        return ticketDao.getAllTickets();
    }

    public List<Ticket> getTicketsByEmployee(Employee employee) {
        return ticketDao.getTicketsByEmployee(employee);
    }

    public Ticket getTicketById(String id) {
        try {
            Ticket ticket = ticketDao.getTicketById(id);
            if (ticket == null) {
                throw new Exception("Ticket with id " + id + " not found");
            }
            return ticket;
        } catch (Exception e) {
            System.err.println("Error in TicketService while fetching ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket addTicket(Ticket ticket) {
        try {
            Ticket returnTicket = ticketDao.addTicket(ticket);
            if (returnTicket == null) {
                throw new Exception("Failed to add ticket");
            }
            return returnTicket;
        } catch (Exception e) {
            System.err.println("Error in EmployeeService while adding employee: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket removeTicket(Ticket ticket) {
        try {
            Ticket returnTicket = ticketDao.removeTicket(ticket);
            if (returnTicket == null) {
                throw new Exception("Failed to remove ticket");
            }
            return returnTicket;
        } catch (Exception e) {
            System.err.println("Error in TicketService while removing ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public Ticket updateTicket(Ticket ticket) {
        try {
            Ticket returnTicket = ticketDao.updateTicket(ticket);
            if (returnTicket == null) {
                throw new Exception("Failed to update ticket");
            }
            return returnTicket;
        } catch (Exception e) {
            System.err.println("Error in TicketService while updating ticket: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}
