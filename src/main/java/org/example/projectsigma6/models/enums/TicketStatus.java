package org.example.projectsigma6.models.enums;

public enum TicketStatus {

    OPEN,
    CLOSED,
    IN_PROGRESS;

    @Override
    public String toString() {
        String name = name();
        String[] words = name.split("_");
        StringBuilder formattedName = new StringBuilder();

        for (String word : words) {
            formattedName.append(word.charAt(0))
                    .append(word.substring(1)
                            .toLowerCase());
            formattedName.append(" ");
        }
        return formattedName.toString().trim();
    }

}
