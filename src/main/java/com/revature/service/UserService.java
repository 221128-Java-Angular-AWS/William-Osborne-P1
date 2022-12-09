package com.revature.service;


import com.revature.exceptions.UsernameExistsException;
import com.revature.persistence.UserDao;
import com.revature.pojos.User;

import java.util.Scanner;

/*
methods that apply to the user should be created here
also where the
 */
public class UserService {
    //Create the data access object
    private UserDao dao;

    public UserService(UserDao dao){
        this.dao = dao;
    }

    public void registerNewUser(User user) throws UsernameExistsException {
        // just some skeleton testing here now
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter a username");
        String username = sc.nextLine();
        user.setUsername(username);
        dao.fetchUserForRegistration(user);






        sc.close();
    }


}
