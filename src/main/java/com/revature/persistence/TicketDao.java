package com.revature.persistence;

import com.revature.pojos.Ticket;
import com.revature.pojos.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/*
# DONE # submit ticket (create new)
# DONE # manager view pending tickets
# DONE # manager approve or deny pending tickets
# DONE # employee view all previous submissions (for that employee)
# DONE # employee filter ticket by status

Stretch
add reimbursement type into ticket submission
employee filter by submission type
add date as well possibly
*/


public class TicketDao {
    // create a Connection object
    private Connection connection;

    public TicketDao() {
        // get the connection object from the connection manager
        this.connection = ConnectionManager.getConnection();
    }


    public boolean submitTicket(Ticket ticket) {
        // create a new ticket in the database, return true upon success
        try {
            // build the sql statement to be passed into PreparedStatement
            String sql = "INSERT INTO tickets (amount, description, user_id) VALUES (?, ?, ?);";

            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // get ticket amount and description from the ticket object and user id from the user object
            pstmt.setDouble(1, ticket.getAmount());
            pstmt.setString(2, ticket.getDescription());
            pstmt.setInt(3, ticket.getUserId());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ticket.setTicketId(rs.getInt("ticket_id"));
            }

            return true;


        } catch (SQLException e) {
            return false;
        }
    }


    // need managers to be able to get all pending tickets
    // need managers to be able to update ticket status
    // need employees to be able to view all previous submissions
    // as well as filter by status
    public TreeSet<Ticket> getPendingTickets(Ticket ticketStatus) {
        // for managers to get all pending tickets
        try {
            PreparedStatement pstmt;
            if (ticketStatus.getStatus() != null) {
                String sql = "SELECT * FROM tickets WHERE status = ?;";
                pstmt = connection.prepareStatement(sql);
                pstmt.setString(1, ticketStatus.getStatus());
            }  else{
                String sql = "SELECT * FROM tickets;";
                pstmt = connection.prepareStatement(sql);
            }

            ResultSet rs = pstmt.executeQuery();

            TreeSet<Ticket> tickets = new TreeSet<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setAmount(rs.getDouble("amount"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setUserId(rs.getInt("user_id"));
                tickets.add(ticket);
            }

            return tickets;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer updateTicketStatus(Ticket ticket) {
        // allow managers to select a ticket from the pending tickets and approve or deny them
        // pending filter in query as safeguard to prevent changing status if status is not pending
        try {
            String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ? AND status = 'pending';";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ticket.getStatus());
            pstmt.setInt(2, ticket.getTicketId());

            pstmt.execute();

            return pstmt.getUpdateCount();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public TreeSet<Ticket> getEmployeeTickets(Integer userId) {
        // for users to get all tickets
        try {
            String sql = "SELECT * FROM tickets WHERE user_id = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            TreeSet<Ticket> tickets = new TreeSet<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setAmount(rs.getDouble("amount"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setUserId(rs.getInt("user_id"));
                tickets.add(ticket);
            }

            return tickets;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public TreeSet<Ticket> getEmployeeTickets(Integer userId, String status) {
        // for users to get tickets filtered by status
        try {
            String sql = "SELECT * FROM tickets WHERE user_id = ? AND status = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            TreeSet<Ticket> tickets = new TreeSet<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setAmount(rs.getDouble("amount"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setUserId(rs.getInt("user_id"));
                tickets.add(ticket);
            }

            return tickets;


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
