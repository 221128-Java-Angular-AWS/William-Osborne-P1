package com.revature.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.persistence.UserDao;
import com.revature.pojos.User;
import com.revature.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class UserAuthServlet extends HttpServlet {
    private UserService service;
    private ObjectMapper mapper;


    @Override
    public void init() throws ServletException {
        this.service = new UserService(new UserDao());
        this.mapper = new ObjectMapper();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        handle user authentication here
        User should be allowed to provide a username and password
        in return the user should get a cookie with the relevant information
        userId, username, userRole,
         */

        // String builder to build the json from the post body
        // this is what will be passed to the ObjectMapper to create the user object
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        while (reader.ready()) {
            jsonBuilder.append(reader.readLine());
        }

        // create a user object using ObjectMapper and the body of the post request (in jsonBuilder object
        User user = mapper.readValue(jsonBuilder.toString(), User.class);

        // authenticate user here
        try {
            User authenticatedUser = service.authenticateUser(user);
            resp.getWriter().print("Login successful " + authenticatedUser.getUserId());
            // create a cookie with the user id, this can be used later to associate users with actions and permissions
            Cookie authCookie = new Cookie("userId", user.getUserId().toString());
            Cookie roleCookie = new Cookie("userRole", user.getUserRole());
            // add the cookie to the response, for now it won't do anything since everything is done in postman
            resp.addCookie(authCookie);
            resp.addCookie(roleCookie);
            resp.setStatus(200); // 200 ok
        } catch (UserNotFoundException e) {
            resp.getWriter().print("Invalid username, if new user please register first.");
            resp.setStatus(401); // forbidden
        } catch (IncorrectPasswordException e) {
            resp.getWriter().print("Incorrect password.");
            resp.setStatus(401); // forbidden
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // TODO: create the update user servlet here after mvp met
        // get the logged in user's id from the cookie
        Integer userId = -1;
        Cookie[] cookies = req.getCookies();
        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("userId")) {
                Cookie authCookie = cookies[i];
                userId = Integer.parseInt(authCookie.getValue());
            }
        }

        // create an object of the existing user information as well as a user from the body of the request
        // this user object will populate null fields in the update user object
        User existingUser = new User(userId);
        existingUser = service.fetchUser(existingUser);

        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader reader = req.getReader();

        while (reader.ready()) {
            jsonBuilder.append(reader.readLine());
        }

        // TODO this logic could be moved to the service layer
        // update null fields so that a complete user object can be sent to update the persistance
        User userUpdate = mapper.readValue(jsonBuilder.toString(), User.class);
        userUpdate.setUserId(existingUser.getUserId());
        if (userUpdate.getUsername() == null || userUpdate.getUsername().equals("")) {
            userUpdate.setUsername(existingUser.getUsername());
        }
        if (userUpdate.getPassword() == null || userUpdate.getPassword().equals("")) {
            userUpdate.setPassword(existingUser.getPassword());
        }
        // for now roles cannot be changed
        userUpdate.setUserRole(existingUser.getUserRole());

        try {
            service.updateUser(userUpdate);
            resp.getWriter().print("User successfully updated.");
            resp.setStatus(200); // 200 OK
        } catch (UsernameExistsException e) {
            resp.getWriter().print("That username is already in use.");
            resp.setStatus(401); // forbidden
            throw new RuntimeException(e);
        }


    }
}
