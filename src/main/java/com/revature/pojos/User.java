package com.revature.pojos;

public class User {
    private Integer userId;
    private String username;
    private String password;
    private String userRole;

    /*
    user object will match similar to the database
    user id will be set by the database so idk if I need a setter for it
    once I stub out some more functionality in the userDAO I will know more
     */

    public User() {

    }

    public User(int userId, String username, String password, String userRole){
        // all args constructor will never get used
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.userRole = userRole;
    }


    public User (String username, String password) {
        // username and password constructor for normal login
        this.username = username;
        this.password = password;
    }


    public User(String username){
        // username only constructor for registration
        // use to check if username exists before allowing password creation
        this.username = username;
    }

    // getters and setters, probably autogenerate for ticket
    public Integer getUserId(){
        return this.userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getUsername(){
        return this.username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUserRole(){
        return userRole;
    }

    public void setUserRole(String userRole){
        this.userRole = userRole;
    }



}
