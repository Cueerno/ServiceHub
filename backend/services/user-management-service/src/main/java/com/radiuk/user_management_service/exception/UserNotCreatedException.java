package com.radiuk.user_management_service.exception;

public class UserNotCreatedException extends RuntimeException {

    public UserNotCreatedException() {
        super();
    }

    public UserNotCreatedException(String message) {
        super(message);
    }

    public UserNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotCreatedException(Throwable cause) {
        super(cause);
    }

    protected UserNotCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
