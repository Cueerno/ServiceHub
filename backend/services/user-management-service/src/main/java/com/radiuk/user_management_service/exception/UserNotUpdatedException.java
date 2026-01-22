package com.radiuk.user_management_service.exception;

public class UserNotUpdatedException extends RuntimeException {

    public UserNotUpdatedException() {
        super();
    }

    public UserNotUpdatedException(String message) {
        super(message);
    }

    public UserNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotUpdatedException(UserNotUpdatedException cause) {
        super(cause);
    }

    protected UserNotUpdatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
