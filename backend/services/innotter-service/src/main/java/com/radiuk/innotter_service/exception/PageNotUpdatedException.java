package com.radiuk.innotter_service.exception;

public class PageNotUpdatedException extends RuntimeException {
  public PageNotUpdatedException(String message) {
    super(message);
  }
}
