package org.choresify.domain.exception;

public final class NoSuchEntityException extends IllegalArgumentException {

  public NoSuchEntityException(String message) {
    super(message);
  }
}
