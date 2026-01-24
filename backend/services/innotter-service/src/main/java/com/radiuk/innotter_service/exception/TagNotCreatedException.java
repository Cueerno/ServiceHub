package com.radiuk.innotter_service.exception;

public class TagNotCreatedException extends RuntimeException {

    public TagNotCreatedException() {
        super();
    }

    public TagNotCreatedException(String message) {
        super(message);
    }

    public TagNotCreatedException(Throwable cause) {
        super(cause);
    }

    public TagNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TagNotCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
