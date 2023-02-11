package org.choresify.domain.exception;

public final class InvariantViolationException extends IllegalArgumentException {
  InvariantViolationException(String message) {
    super(message);
  }
}
