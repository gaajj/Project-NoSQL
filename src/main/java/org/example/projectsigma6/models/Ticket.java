package org.example.projectsigma6.models;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.example.projectsigma6.models.enums.TicketPriority;
import org.example.projectsigma6.models.enums.TicketStatus;
import org.example.projectsigma6.models.enums.TicketType;

import java.util.Date;

public class Ticket {

    @BsonId
    private ObjectId id;
    private String title;
    private String description;
    private TicketType type;
    private TicketStatus status;
    private TicketPriority priority;
    private Employee createdBy;
    private Employee assignedTo;
    private Date dueDate;

    public Ticket() {}

    public Ticket(@BsonProperty("id") ObjectId id,
                  @BsonProperty("title") String title,
                  @BsonProperty("description") String description,
                  @BsonProperty("type") TicketType type,
                  @BsonProperty("status") TicketStatus status,
                  @BsonProperty("priority") TicketPriority priority,
                  @BsonProperty("createdBy") Employee createdBy,
                  @BsonProperty("assignedTo") Employee assignedTo,
                  @BsonProperty("dueDate") Date dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.status = status;
        this.priority = priority;
        this.createdBy = createdBy;
        this.assignedTo = assignedTo;
        this.dueDate = dueDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TicketType getType() {
        return type;
    }

    public void setType(TicketType type) {
        this.type = type;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPriority getPriority() {
        return priority;
    }

    public void setPriority(TicketPriority priority) {
        this.priority = priority;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", status='" + status + '\'' +
                ", priority='" + priority + '\'' +
                ", createdBy=" + createdBy +
                ", assignedTo=" + assignedTo +
                ", dueDate=" + dueDate +
                '}';
    }

    public String toStringShort() {
        return "Ticket{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", createdBy=" + (createdBy != null ? createdBy.toStringShort() : "null") +
                ", assignedTo=" + (assignedTo != null ? assignedTo.toStringShort() : "null") +
                '}';
    }

}
