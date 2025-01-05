package org.example.projectsigma6.models.enums;

public enum TicketType {

    FEATURE,
    BUG,
    TASK;

    @Override
    public String toString() {
        String name = name();
        return name.charAt(0) + name.substring(1).toLowerCase();
    }

}
