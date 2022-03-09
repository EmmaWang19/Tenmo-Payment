package com.techelevator.tenmo.model;

public class NotAuthorizedException extends Exception{
    public NotAuthorizedException(){
        super("Not Authorized For This Operation.");
    }
}
