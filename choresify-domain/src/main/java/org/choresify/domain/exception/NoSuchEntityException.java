package org.choresify.domain.exception;

public final class NoSuchEntityException extends RuntimeException {

  public NoSuchEntityException(String message) {
    super(message);
  }
}
