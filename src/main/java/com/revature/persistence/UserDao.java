package com.revature.persistence;


import com.revature.exceptions.IncorrectPasswordException;
import com.revature.exceptions.UserNotFoundException;
import com.revature.exceptions.UsernameExistsException;
import com.revature.pojos.User;

import java.sql.*;

/*
operations needed:
create user to be used for user new user registration
update user to be used for any changes made to the user
    at any given time that a user is successfully logged in there should be a complete user
    object, any changes that the user wants to make to their user account should be reflected
    in the user object so that the whole object can be written to the database and reflect any
    changes appropriately
 */

public class UserDao {
    // need the connection object to be able to communicate with the database
    private Connection connection;


    public UserDao(){
        // actually get the connection object from the connection manager
        this.connection = ConnectionManager.getConnection();
    }


    public void createUser(User user){
        // Create a new user and write to the database, upon writing user to the database complete the user
        // object by getting the serial user_id from the created record and assigning that to the user object
        try {
            // build the sql statement to be passed into the PreparedStatement interface object
            String sql = "INSERT INTO users (username, password, user_role) VALUES (?, ?, ?);";

            // need to user the optional parameter to return generated keys for the serial user_id
            PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            // parse the prepared statement from the user object's properties
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUserRole());

            // execute the prepared statement which will then set generated keys to the pstmt object
            pstmt.executeUpdate();

            // get the serial user_id
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()){
                user.setUserId(rs.getInt("user_id"));
            }


        } catch (SQLException e) {
            System.out.println("Error parsing the prepared statement when creating a new user");
        }
    }


    public void updateUser(User user){
        // update the user records to reflect any changes made to the user (change username, password...)
        try {
            //build the sql statement (String sql is in method scope)
            String sql =
                    "UPDATE users " +
                    "SET (username, password, user_role) " +
                    "VALUES (?, ?, ?) " +
                    "WHERE user_id = ?;";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUserRole());
            pstmt.setString(4, user.getUserId().toString());

            pstmt.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Error parsing the prepared statement when updating user records");
        }
    }


    public User fetchUserForRegistration(User user) throws UsernameExistsException{
        // the user object should only have a user.username value
        // fetch this from the database
        // if the result set is empty the username is free and the end user can proceed with registration
        // otherwise throw a UsernameExists exception
        try {
            String sql = "SELECT username FROM users WHERE username LIKE ?";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()){
                throw new UsernameExistsException("That username already exists.");
            }
            return user;


        } catch (SQLException e) {
            System.out.println("Error parsing the prepared statement when updating user records");
            e.printStackTrace();
        }
        return null;
    }



    public User AuthenticateUser(User user) throws UserNotFoundException, IncorrectPasswordException {
        // fetch the user record by username here to be passed around for manipulation
        // should populate the user object for use at the service layer
        // authentication, password change, username change should apply to the object and
        // be persisted by the updateUser method above
        try {
            // build the sql string to get user by username
            // (String sql is in method scope)
            String sql = "SELECT * FROM users WHERE username = ?;";

            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());

            // get the result set and use that to initialize the user object
            // if the username exists as well as if the password is correct for that user
            ResultSet rs = pstmt.executeQuery();

            // deal with username not found
            if (rs.next()) {
                if (user.getPassword().equals(rs.getString("password"))) {
                    user.setUserId(rs.getInt("user_id"));
                    user.setUserRole(rs.getString("user_role"));
                    return user;
                } else {
                    throw new IncorrectPasswordException("Incorrect password");
                }
            } else {
                throw new UserNotFoundException("Username not found.");
            }


        } catch (SQLException e) {
            System.out.println("Error parsing the prepared statement when fetching user records");
            e.printStackTrace();
        }
        return null;
    }


}
