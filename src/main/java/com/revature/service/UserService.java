package com.revature.service;


import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.persistence.UserDao;
import com.revature.pojos.User;

import java.util.Scanner;

/*
methods that apply to the user should be created here
 ## register
 ## authenticate
 ## update
 */
public class UserService {
    //Create the data access object
    private UserDao dao;


    public UserService(UserDao dao){
        // Constructor for the service that initializes a dao object
        this.dao = dao;
    }

    public User registerNewUser(User user) throws UsernameExistsException {
        // register a new user
        // if username exists an exception will be caught and user will be returned null
        // do not continue if user is null
        user = dao.fetchUserForRegistration(user);

        // if the user.userRole is null set to employee
        if (user.getUserRole() == null) {
            user.setUserRole("employee");
        }

        dao.createUser(user);
        return user;
    }

    public User authenticateUser(User user) throws UserNotFoundException, IncorrectPasswordException {
        // @TODO this is nothing, make it actually authenticate user
        user = dao.AuthenticateUser(user);
        // I dont think I need the if statement due to the exception but will try with and without
        if (!(user == null)) {
            return user;
        }
        return null;
    }


    public User updateUser(User user) {
        dao.updateUser(user);
        return user;
    }






}
