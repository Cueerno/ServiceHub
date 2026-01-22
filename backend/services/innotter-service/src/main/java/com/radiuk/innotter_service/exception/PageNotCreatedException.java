package com.radiuk.innotter_service.exception;

public class PageNotCreatedException extends RuntimeException {

    public PageNotCreatedException() {
        super();
    }

    public PageNotCreatedException(String message) {
        super(message);
    }

    public PageNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageNotCreatedException(Throwable cause) {
        super(cause);
    }

    protected PageNotCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
