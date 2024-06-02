package com.stellarlabs.authentication_and_authorization_service.exception.customExceptions;


public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException() {
    }


    public UserNotFoundException(String message) {
        super(message);
    }

}
