package org.choresify.domain.exception;

public sealed class DomainException extends RuntimeException {
  public DomainException(String message) {
    super(message);
  }

  public static final class ValidationException extends DomainException {
    public ValidationException(String message) {
      super(message);
    }
  }

  public static final class FailedPreconditionException extends DomainException {
    public FailedPreconditionException(String message) {
      super(message);
    }
  }
}
