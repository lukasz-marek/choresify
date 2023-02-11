package org.choresify.domain.exception;

public final class InvariantViolationException extends IllegalArgumentException {
  public InvariantViolationException(String message) {
    super(message);
  }
}
