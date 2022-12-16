package com.revature.persistence;



/*
submit ticket (create new)
manager view pending tickets
manager approve or deny pending tickets
employee view all previous submissions (for that employee)
employee filter ticket by status
*/

import com.revature.pojos.Ticket;
import com.revature.pojos.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

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
            System.out.println(pstmt.toString());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                ticket.setTicketId(rs.getInt("ticket_id"));
            }

            return true;




        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Ticket submission failed");
            return false;
        }
    }


    // need managers to be able to get all pending tickets
    // need managers to be able to update ticket status
    // need employees to be able to view all previous submissions
    // as well as filter by status
    //@TODO add date into ticketing system after mvp
    public Set<Ticket> getPendingTickets() {
        // for managers to get all pending tickets
        try {
            String sql = "SELECT * FROM tickets WHERE status = 'pending' ORDER BY ticket_id;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            Set<Ticket> tickets = new HashSet<>();
            while (rs.next()) {
                Ticket ticket = new Ticket();
                ticket.setTicketId(rs.getInt("ticket_id"));
                ticket.setAmount(rs.getDouble("amount"));
                ticket.setDescription(rs.getString("description"));
                ticket.setStatus(rs.getString("status"));
                ticket.setUserId(rs.getInt("user_id"));
                tickets.add(ticket);
            }
            System.out.println(tickets);

            return tickets;


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Integer updateTicketStatus(Ticket ticket) {
        // allow managers to select a ticket from the pending tickets and approve or deny them
        // pending filter in query as safeguard to prevent changing status if not pending
        try {
            String sql = "UPDATE tickets SET status = ? WHERE ticket_id = ? AND status = 'pending';";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, ticket.getStatus());
            pstmt.setInt(2, ticket.getTicketId());

            pstmt.execute();

            return pstmt.getUpdateCount();





        } catch (SQLException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }

    }


    public Set<Ticket> getEmployeeTickets(Integer userId) {
        // for users to get all tickets
        try {
            String sql = "SELECT * FROM tickets WHERE user_id = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            Set<Ticket> tickets = new HashSet<>();
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
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }


    public Set<Ticket> getEmployeeTickets(Integer userId, String status) {
        // for users to get tickets filtered by status
        try {
            String sql = "SELECT * FROM tickets WHERE user_id = ? AND status = ?;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            Set<Ticket> tickets = new HashSet<>();
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
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }




}
