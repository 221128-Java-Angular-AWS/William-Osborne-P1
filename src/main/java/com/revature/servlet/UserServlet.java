package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.UsernameExistsException;
import com.revature.pojos.User;
import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// make sure to include response codes in operations

public class UserServlet extends HttpServlet {
    private UserService service;
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // register user
        // create a user opject from the input (username and password web input)
        // @TODO this just does not work right now, try updating using the revagenda demo as a guide
        User user = mapper.readValue(req.getInputStream(), User.class);
        try {
            User registeredUser = service.registerNewUser(user);
            System.out.println(registeredUser.toString());
        } catch (UsernameExistsException e) {
            //resp.getWriter()
            // status 403 is forbidden, they do not have access to that username
            resp.setStatus(403);
        }



    }


}
