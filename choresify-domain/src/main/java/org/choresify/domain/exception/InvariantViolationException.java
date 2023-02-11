package org.choresify.domain.exception;

public class InvariantViolationException extends IllegalArgumentException {
  public InvariantViolationException(String message) {
    super(message);
  }
}
