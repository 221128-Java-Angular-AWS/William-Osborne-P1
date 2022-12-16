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

public class ManagerTicketServlet extends HttpServlet {
    private TicketService ticketService;
    private UserService userService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.ticketService = new TicketService(new TicketDao());
        //this.userService = new UserService(new UserDao());
        this.mapper = new ObjectMapper();
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         get all pending tickets
         user must be a manager so need to create the user object from the user id in the cookie
            alternatively could probably put user role into cookie
         give managers a list of pending tickets ordered by ticket id allowing for priority based on submission order
         */
        Integer userId = -1;
        String userRole = null;
        Cookie[] cookies = req.getCookies();

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                // TODO this is in more than one place and needs to be changed as done in else below
                Cookie authCookie = cookies[i];
                userId = Integer.parseInt(authCookie.getValue());
            } else if(cookies[i].getName().equals("userRole")) {
                userRole = cookies[i].getValue();
            }
        }

        // again I dont need the class object here since I already have the user id and user role
        //User user = new User(userId, userRole);

        // no need to read the body of the request since it is there are no arguments to pass only
        // verification that the user is a manager
        System.out.println(userRole);
        if (userRole.equals("manager")) {
            Set<Ticket> pendingTickets = ticketService.managerViewTickets();

            String json = mapper.writeValueAsString(pendingTickets);
            System.out.println(json);
            resp.getWriter().println(json);
            resp.setStatus(200); // 200 OK
        } else {
            resp.getWriter().print("Only managers can view all pending tickets.");
            resp.setStatus(403); //403 forbidden
        }
    }


    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        update the status of a ticket based on passed ticket id and updated status in put body
        write these changes to the database
         */
        // TODO: add column to the database to track manager user id that updated ticket after mvp
        // for now capture user id from cookie but nothing will be done with it
        Integer userId = -1;
        String userRole = null;
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                userId = Integer.parseInt(cookies[i].getValue());
            } else if (cookies[i].getName().equals("userRole")) {
                userRole = cookies[i].getValue();
            }
        }

        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        while (reader.ready()) {
            jsonBuilder.append(reader.readLine());
        }

        System.out.println(jsonBuilder);
        // ObjectMapper makes this easy to read into a map instead of a custom class
        // only allow one ticketId at a time, maybe revisit and see if there is a way to do more than one at a time
        // ticketId and status in body json
        // might as well make a ticket object out of this
        Ticket ticket = mapper.readValue(jsonBuilder.toString(), Ticket.class);

        Integer rowsUpdated = ticketService.managerUpdateTicket(ticket);

        if (rowsUpdated > 0) {
            resp.getWriter().println("Ticket successfully updated");
            resp.setStatus(200); // 200 OK
        } else{
            resp.getWriter().print("Ticket update failed. Check ticket id and status. Only pending tickets may be updated");
            resp.setStatus(401); // forbidden
        }

    }
}
