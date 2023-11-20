package com.volasoftware.tinder.exception;

public class InvalidVerificationTokenException extends RuntimeException{
    public InvalidVerificationTokenException(String message){
        super(message);
    }
}
