package com.stellarlabs.authentication_and_authorization_service.exception.customExceptions;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException() {
    }

    public PermissionDeniedException(String message) {
        super(message);
    }

}
