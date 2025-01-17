package org.example.projectsigma6.tests;

import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.*;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;
import org.example.projectsigma6.services.TicketService;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ExampleTicket {

    private static TicketService ticketService;
    private static EmployeeService employeeService;

    public static void main(String[] args) {
        ticketService = ServiceManager.getInstance().getTicketService();
        employeeService = ServiceManager.getInstance().getEmployeeService();
        exampleAddTicket();
    }

    public static void exampleGetTicketById() {
        Ticket ticket = ticketService.getTicketById("678290eafabd91794c7b5da7");
        System.out.println(ticket.toStringShort());
    }

    public static void exampleGetAllTickets() {
        List<Ticket> tickets = ticketService.getAllTickets();
        for (Ticket ticket : tickets) {
            System.out.println(ticket);
        }
    }

    public static void exampleAddTicket() {

        Employee createdBy = employeeService.getEmployeeByUsername("jan.de.molen");
        Employee assignedTo = employeeService.getEmployeeByUsername("charlie.williams");

        Ticket ticket = new Ticket();
        ticket.setId(new ObjectId());
        ticket.setTitle("Meow");
        ticket.setDescription("What the sigma");
        ticket.setType(TicketType.TASK);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        ticket.setPriority(TicketPriority.HIGH);
        ticket.setCreatedBy(createdBy);
        ticket.setAssignedTo(assignedTo);
        ticket.setCreatedAt(new Date());
        ticket.setDueDate(new GregorianCalendar(2025, Calendar.APRIL, 20).getTime());
        ticket.setDeleted(false);

        ticketService.addTicket(ticket);
    }

    public static void exampleRemoveTicket() {
        Ticket ticket = ticketService.getTicketById("6782a37343375677a2dce2ad");
        ticketService.removeTicket(ticket);
    }

    public static void exampleUpdateTicket() {
        Ticket ticket = ticketService.getTicketById("6782a37343375677a2dce2ad");
        ticket.setTitle("Meow Meow Meow Meow Meow");
        ticket.setDescription("What the Skibidibi");

        ticketService.updateTicket(ticket);
    }

    public static void exampleAdd3Tickets() {
        // Ticket 1
        Employee createdBy1 = employeeService.getEmployeeByUsername("jane.smith");
        Employee assignedTo1 = employeeService.getEmployeeByUsername("john.doe");

        Ticket ticket1 = new Ticket(
                new ObjectId(),                       // _id for ticket
                "System Bug",                          // title
                "There is a bug in the login system.", // description
                TicketType.BUG,                        // type
                TicketStatus.OPEN,                     // status
                TicketPriority.HIGH,                   // priority
                createdBy1,                            // createdBy
                assignedTo1,                           // assignedTo
                new Date(),
                new Date(),                            // current date as dueDate
                false
        );

        // Ticket 2
        Employee createdBy2 = employeeService.getEmployeeByUsername("bob.brown");
        Employee assignedTo2 = employeeService.getEmployeeByUsername("jane.smith");

        Ticket ticket2 = new Ticket(
                new ObjectId(),                              // _id for ticket
                "Feature Request",                           // title
                "Request to add new feature for user profiles.", // description
                TicketType.FEATURE,                          // type
                TicketStatus.IN_PROGRESS,                    // status
                TicketPriority.MEDIUM,                       // priority
                createdBy2,                                  // createdBy
                assignedTo2,                                 // assignedTo
                new Date(),
                new Date(System.currentTimeMillis() + 86400000),// dueDate set to 1 day from now
                false
        );

        // Ticket 3
        Employee createdBy3 = employeeService.getEmployeeByUsername("alice.johnson");
        Employee assignedTo3 = employeeService.getEmployeeByUsername("bob.brown");

        Ticket ticket3 = new Ticket(
                new ObjectId(),                       // _id for ticket
                "Server Downtime",                     // title
                "Server has been down for 30 minutes.", // description
                TicketType.BUG,                        // type
                TicketStatus.CLOSED,                    // status
                TicketPriority.HIGH,                   // priority
                createdBy3,                            // createdBy
                assignedTo3,                           // assignedTo
                new Date(),
                new Date(System.currentTimeMillis() - 86400000), // dueDate set to 1 day ago
                false
        );

        // Add tickets to the database (this will depend on your ticketService implementation)
        ticketService.addTicket(ticket1);
        ticketService.addTicket(ticket2);
        ticketService.addTicket(ticket3);
    }

}
