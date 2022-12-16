package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.persistence.TicketDao;
import com.revature.persistence.UserDao;
import com.revature.pojos.Ticket;
import com.revature.pojos.User;
import com.revature.service.TicketService;
import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class EmployeeTicketServlet extends HttpServlet {
    private TicketService ticketService;
    private UserService userService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.ticketService = new TicketService(new TicketDao());
        this.userService = new UserService(new UserDao());
        this.mapper = new ObjectMapper();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Submit new ticket
        user must be logged in as employee
        Get information from cookie:
            user id - use with userDao to create a User object to verify role
            alternatively could include user id and role in cookie to avoid having to query users
        Get information from body:
            ticket amount
            ticket description
            status pending
        create ticket object and submit to submitTicket in dao
         */

        // get the user id from the cookie (associated with the login session)
        // should anyone be able to submit a ticket or only employees?
        // if anyone can submit a ticket managers will need additional filtering to
        // prevent updating their own tickets
        Integer userId = -1;
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                Cookie authCookie = cookies[i];
                userId = Integer.parseInt(authCookie.getValue());
            }
        }

        // pull information our of the request
        // get info from the JSON body and map it to the object
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        // create the ticket object from the body of the request
        while (reader.ready()) {
            jsonBuilder.append(reader.readLine());
        }

        Ticket ticket = mapper.readValue(jsonBuilder.toString(), Ticket.class);
        ticket.setUserId(userId);

        // for now get the userId from the cookie and pass that in with the ticket submission
        // later I might want to create a user object and authenticate it against the database
        // prior to submission for added security
        boolean status = false;

        // TODO: mvp met, update to create a response to indicate to the user why submission failed
        if (userId != -1) {
            status = ticketService.submitTicket(ticket);
        }

        if (status) {
            resp.getWriter().print("Ticket submission successful");
            resp.setStatus(201); // 201 created
        } else {
            resp.getWriter().print("Ticket submission failed. Make sure to include both a description and an amount.");
            resp.setStatus(401); // 401 unauthorized
        }
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // get the user id from the cookie (associated with the login session)
        Integer userId = -1;
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                Cookie authCookie = cookies[i];
                userId = Integer.parseInt(authCookie.getValue());
            }
        }

        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        while (reader.ready()) {
            // test with empty body
            jsonBuilder.append(reader.readLine());
        }

        // create a Ticket object to pass values into the ticketService
        Ticket ticket = mapper.readValue(jsonBuilder.toString(), Ticket.class);

        // this will send a user id and either null or a desired status to filter tickets by
        // TreeSet used for sorted results, if implementing any more filter/sort features a queue would be better
        // for this since the order could relay solely on the query results
        TreeSet<Ticket> userTickets = ticketService.viewTickets(userId, ticket.getStatus());

        String json = mapper.writeValueAsString(userTickets);
        resp.getWriter().println(json);
        resp.setStatus(200); // 200 OK

    }
}
