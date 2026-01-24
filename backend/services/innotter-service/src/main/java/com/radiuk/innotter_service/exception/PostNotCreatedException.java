package com.radiuk.innotter_service.exception;

public class PostNotCreatedException extends RuntimeException {

    public PostNotCreatedException() {
        super();
    }

    public PostNotCreatedException(String message) {
        super(message);
    }

    public PostNotCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostNotCreatedException(Throwable cause) {
        super(cause);
    }

    protected PostNotCreatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
