package org.example.projectsigma6.services;

import com.mongodb.client.MongoClient;
import org.example.projectsigma6.dao.EmployeeDao;
import org.example.projectsigma6.dao.TicketDao;
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

    public Ticket addTicket(Ticket ticket) {
        return ticketDao.addTicket(ticket);
    }

}
