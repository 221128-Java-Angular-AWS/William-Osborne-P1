package com.revature.service;

import com.revature.persistence.TicketDao;
import com.revature.pojos.Ticket;
import com.revature.pojos.User;

import java.util.Set;

public class TicketService {
    /*
    Ticket services:
    # DONE # employee submit ticket (amount, description, default status of pending)
    # DONE # employee view tickets filtered by status (for now only one status at a time, could method
        overload in DAO for multiple ticket status view)
    # DONE # manager view queue of pending tickets (for now order by id, later maybe add date and order by that instead)
    # DONE # manager update ticket (id, new status)
    */

    private TicketDao dao;

    public TicketService(TicketDao dao) {
        // Constructor for the service that initializes a dao object
        this.dao = dao;
    }

    public boolean submitTicket(Ticket ticket) {
        // userId is populated in the ticket object from the cookie giving the ticket object everything needed
        return dao.submitTicket(ticket);

    }


    public Set<Ticket> viewTickets(Integer userId, String status) {
        // returns a set of previously submitted tickets for the user of a specified status or all submitted
        // tickets if no status is specified
        if (status != null) {
            System.out.println(status);
            Set<Ticket> tickets = dao.getEmployeeTickets(userId, status);
            return tickets;
        } else {
            System.out.println("any status");
            Set<Ticket> tickets = dao.getEmployeeTickets(userId);
            return tickets;
        }
    }


    public Set<Ticket> managerViewTickets() {
        Set<Ticket> tickets = dao.getPendingTickets();
        return tickets;
    }


    public Integer managerUpdateTicket(Ticket ticket) {

        Integer rowsUpdated = dao.updateTicketStatus(ticket);
        return rowsUpdated;
    }


}
