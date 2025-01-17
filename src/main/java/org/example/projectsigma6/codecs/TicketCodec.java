package org.example.projectsigma6.codecs;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.Employee;
import org.example.projectsigma6.models.Ticket;
import org.example.projectsigma6.models.enums.*;

import java.util.Date;

public class TicketCodec implements Codec<Ticket> {

    private final EmployeeCodec employeeCodec = new EmployeeCodec();

    @Override
    public Ticket decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        ObjectId id = new ObjectId(reader.readString("_id"));
        String title = reader.readString("title");
        String description = reader.readString("description");
        String typeString = reader.readString("type"); // String
        TicketType type = TicketType.valueOf(typeString); // String -> Enum
        String statusString = reader.readString("status"); // String
        TicketStatus status = TicketStatus.valueOf(statusString); // String -> Enum
        String priorityString = reader.readString("priority"); // String
        TicketPriority priority = TicketPriority.valueOf(priorityString); // String -> Enum
        reader.readName("createdBy");
        Employee createdBy = employeeCodec.decodeEmbedded(reader);
        reader.readName("assignedTo");
        Employee assignedTo = null;
        if (reader.getCurrentBsonType() != BsonType.NULL) {
            assignedTo = employeeCodec.decodeEmbedded(reader);
        } else {
            reader.readNull();
        }
        Date createdAt = new Date(reader.readDateTime("createdAt")); // BSON date -> Date
        Date dueDate = new Date(reader.readDateTime("dueDate")); // BSON date -> Date
        boolean isDeleted = reader.readBoolean("isDeleted");

        reader.readEndDocument();
        return new Ticket(id, title, description, type, status, priority, createdBy, assignedTo, createdAt, dueDate, isDeleted);
    }

    @Override
    public void encode(BsonWriter writer, Ticket ticket, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeString("_id", ticket.getId().toString());
        writer.writeString("title", ticket.getTitle());
        writer.writeString("description", ticket.getDescription());
        writer.writeString("type", ticket.getType().name()); // Enum -> String
        writer.writeString("status", ticket.getStatus().name()); // Enum -> String
        writer.writeString("priority", ticket.getPriority().name()); // Enum -> String
        writer.writeName("createdBy");
        employeeCodec.encodeEmbedded(writer, ticket.getCreatedBy());
        writer.writeName("assignedTo");
            employeeCodec.encodeEmbedded(writer, ticket.getAssignedTo());
        writer.writeDateTime("createdAt", ticket.getCreatedAt().getTime()); // Date -> BSON date
        writer.writeDateTime("dueDate", ticket.getDueDate().getTime()); // Date -> BSON date
        writer.writeBoolean("isDeleted", ticket.isDeleted());

        writer.writeEndDocument();
    }

    @Override
    public Class<Ticket> getEncoderClass() {
        return Ticket.class;
    }

}
