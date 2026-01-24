package com.radiuk.user_management_service.exception;

public class ExpiredPasswordResetTokenException extends RuntimeException {

    public ExpiredPasswordResetTokenException() {
        super();
    }

    public ExpiredPasswordResetTokenException(String message) {
        super(message);
    }

    public ExpiredPasswordResetTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredPasswordResetTokenException(Throwable cause) {
        super(cause);
    }

    protected ExpiredPasswordResetTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
