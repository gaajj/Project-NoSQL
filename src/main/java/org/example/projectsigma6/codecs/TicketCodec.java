package org.example.projectsigma6.codecs;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;
import org.example.projectsigma6.services.EmployeeService;
import org.example.projectsigma6.services.ServiceManager;

import java.util.Date;

public class TicketCodec implements Codec<Ticket> {

    @Override
    public Ticket decode(BsonReader reader, DecoderContext decoderContext) {
        EmployeeService employeeService = ServiceManager.getInstance().getEmployeeService();
        reader.readStartDocument();

        // Read fields from the BSON document
        ObjectId id = new ObjectId(reader.readString("id"));
        String title = reader.readString("title");
        String description = reader.readString("description");
        String typeString = reader.readString("type"); // String
        TicketType type = TicketType.valueOf(typeString); // String -> Enum
        String statusString = reader.readString("status"); // String
        TicketStatus status = TicketStatus.valueOf(statusString); // String -> Enum
        String priorityString = reader.readString("priority"); // String
        TicketPriority priority = TicketPriority.valueOf(priorityString); // String -> Enum
        ObjectId createdById = new ObjectId(reader.readString("createdBy"));
        ObjectId assignedToId = new ObjectId(reader.readString("assignedTo"));
        Date dueDate = new Date(reader.readDateTime("dueDate")); // BSON date -> Date

        // Get employees by ID's
        Employee createdBy = employeeService.getEmployeeById(String.valueOf(createdById));
        Employee assignedTo = employeeService.getEmployeeById(String.valueOf(assignedToId));

        reader.readEndDocument();

        return new Ticket(id, title, description, type, status, priority, createdBy, assignedTo, dueDate);
    }

    @Override
    public void encode(BsonWriter writer, Ticket ticket, EncoderContext encoderContext) {
        writer.writeStartDocument();

        // Write fields to the BSON document
        writer.writeString("id", ticket.getId().toString());
        writer.writeString("title", ticket.getTitle());
        writer.writeString("description", ticket.getDescription());
        writer.writeString("type", ticket.getType().name()); // Enum -> String
        writer.writeString("status", ticket.getStatus().name()); // Enum -> String
        writer.writeString("priority", ticket.getPriority().name()); // Enum -> String
        writer.writeString("createdBy", ticket.getCreatedBy().getId().toString()); // Assuming Employee has getId() method
        writer.writeString("assignedTo", ticket.getAssignedTo().getId().toString()); // Assuming Employee has getId() method
        writer.writeDateTime("dueDate", ticket.getDueDate().getTime()); // Date -> BSON date

        writer.writeEndDocument();
    }

    @Override
    public Class<Ticket> getEncoderClass() {
        return Ticket.class;
    }

}
