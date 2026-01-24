package com.radiuk.innotter_service.exception;

public class PageNotUpdatedException extends RuntimeException {

    public PageNotUpdatedException() {
        super();
    }

    public PageNotUpdatedException(String message) {
        super(message);
    }

    public PageNotUpdatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotUpdatedException(Throwable cause) {
        super(cause);
    }

    protected PageNotUpdatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
