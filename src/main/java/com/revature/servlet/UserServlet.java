package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.UsernameExistsException;
import com.revature.persistence.UserDao;
import com.revature.pojos.User;
import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

// make sure to include response codes in operations

public class UserServlet extends HttpServlet {
    private UserService service;
    private ObjectMapper mapper;
    @Override
    public void init() throws ServletException {
        // @TODO: need to come back and trace back to understand why I am doing this
        this.service = new UserService(new UserDao());
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        Registration of new user. Allows for JOSN body input providing:
            username and password
            username, password and userRole
            userRole defaults to employee if not provided as outlined in the UserService.registerNewUser()
         */

        // pull information our of the request
        // get info from the JSON body and map it to the object
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        while(reader.ready()){
            // append each line from the reader object to the jsonBuilder object
            jsonBuilder.append(reader.readLine());
        }

        // preview the JSON string in the console for troubleshooting
        //System.out.println("JSON string: " + jsonBuilder.toString());
        User user = mapper.readValue(jsonBuilder.toString(), User.class);
        // preview user object for troubleshooting
        //System.out.println(user.toString());

        try {
            // try to register the new user
            User registeredUser = service.registerNewUser(user);
            resp.getWriter().print("Registration successful. Please login with registered credentials.");
            resp.setStatus(201);  // 201 created
        } catch (UsernameExistsException e) {
            // if the username already exists a UsernameExistsException will be caught
            resp.getWriter().print("Username already exists: " + user.getUsername());
            resp.setStatus(403); //403 forbidden
        }
    }

}
