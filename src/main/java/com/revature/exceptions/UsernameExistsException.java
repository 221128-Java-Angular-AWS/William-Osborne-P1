package com.revature.exceptions;

public class UsernameExistsException extends Exception{
    public UsernameExistsException(String msg){
        super(msg);
    }
}
