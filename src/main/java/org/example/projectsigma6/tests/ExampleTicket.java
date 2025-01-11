package org.example.projectsigma6.tests;

import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.*;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.util.Date;
import java.util.List;

public class ExampleTicket {

    private static TicketService ticketService;

    public static void main(String[] args) {
        ticketService = ServiceManager.getInstance().getTicketService();

    }

    public static void exampleGetAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    public static void exampleAdd3Tickets() {
        // Ticket 1
        Employee createdBy1 = new Employee(
                new ObjectId(), "alice.smith", "hashedPassword1", "salt1", "Alice", "Smith",
                "alice.smith@example.com", "+31 20 111 2222", EmployeeType.REGULAR,
                Location.UTRECHT, true
        );
        Employee assignedTo1 = new Employee(
                new ObjectId(), "bob.brown", "hashedPassword2", "salt2", "Bob", "Brown",
                "bob.brown@example.com", "+31 20 333 4444", EmployeeType.REGULAR,
                Location.ROTTERDAM, true
        );

        Ticket ticket1 = new Ticket(
                new ObjectId(),                       // _id for ticket
                "System Bug",                          // title
                "There is a bug in the login system.", // description
                TicketType.BUG,                        // type
                TicketStatus.OPEN,                     // status
                TicketPriority.HIGH,                   // priority
                createdBy1,                            // createdBy
                assignedTo1,                           // assignedTo
                new Date()                              // current date as dueDate
        );

        // Ticket 2
        Employee createdBy2 = new Employee(
                new ObjectId(), "john.doe", "hashedPassword2", "salt2", "John", "Doe",
                "john.doe@example.com", "+31 20 444 5555", EmployeeType.REGULAR,
                Location.AMSTERDAM, true
        );
        Employee assignedTo2 = new Employee(
                new ObjectId(), "jane.doe", "hashedPassword3", "salt3", "Jane", "Doe",
                "jane.doe@example.com", "+31 20 666 7777", EmployeeType.REGULAR,
                Location.UTRECHT, true
        );

        Ticket ticket2 = new Ticket(
                new ObjectId(),                              // _id for ticket
                "Feature Request",                           // title
                "Request to add new feature for user profiles.", // description
                TicketType.FEATURE,                          // type
                TicketStatus.IN_PROGRESS,                    // status
                TicketPriority.MEDIUM,                       // priority
                createdBy2,                                  // createdBy
                assignedTo2,                                 // assignedTo
                new Date(System.currentTimeMillis() + 86400000) // dueDate set to 1 day from now
        );

        // Ticket 3
        Employee createdBy3 = new Employee(
                new ObjectId(), "susan.miller", "hashedPassword4", "salt4", "Susan", "Miller",
                "susan.miller@example.com", "+31 20 777 8888", EmployeeType.REGULAR,
                Location.ROTTERDAM, true
        );
        Employee assignedTo3 = new Employee(
                new ObjectId(), "steve.jones", "hashedPassword5", "salt5", "Steve", "Jones",
                "steve.jones@example.com", "+31 20 999 0000", EmployeeType.SERVICEDESK,
                Location.AMSTERDAM, true
        );

        Ticket ticket3 = new Ticket(
                new ObjectId(),                       // _id for ticket
                "Server Downtime",                     // title
                "Server has been down for 30 minutes.", // description
                TicketType.BUG,                        // type
                TicketStatus.CLOSED,                    // status
                TicketPriority.HIGH,                   // priority
                createdBy3,                            // createdBy
                assignedTo3,                           // assignedTo
                new Date(System.currentTimeMillis() - 86400000) // dueDate set to 1 day ago
        );

        // Add tickets to the database (this will depend on your ticketService implementation)
        ticketService.addTicket(ticket1);
        ticketService.addTicket(ticket2);
        ticketService.addTicket(ticket3);
    }

}
