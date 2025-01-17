package org.example.projectsigma6.controllers;

import org.example.projectsigma6.models.Ticket;

import java.util.List;
import java.util.stream.Collectors;

public class TicketSearchController {

    private List<Ticket> tickets;

    public List<Ticket> searchTickets(String searchText, String searchLogic) {
        if (searchText == null || searchText.trim().isEmpty()) {
            return tickets.stream()
                    .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                    .collect(Collectors.toList());
        }

        String[] keywords = searchText.toLowerCase().trim().split("\\s+");

        return tickets.stream()
                .filter(ticket -> matchesSearch(ticket, keywords, searchLogic))
                .sorted((t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()))
                .collect(Collectors.toList());
    }

    private boolean matchesSearch(Ticket ticket, String[] keywords, String searchLogic) {
        String subject = ticket.getTitle().toLowerCase();
        String content = ticket.getDescription().toLowerCase();

        if ("AND".equals(searchLogic)) {
            for (String keyword : keywords) {
                if (!subject.contains(keyword) && !content.contains(keyword)) {
                    return false;
                }
            }
            return true;
        } else {
            for (String keyword : keywords) {
                if (subject.contains(keyword) && content.contains(keyword)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

}