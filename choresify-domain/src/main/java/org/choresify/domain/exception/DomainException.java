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

  public static final class PreconditionFailedException extends DomainException {
    public PreconditionFailedException(String message) {
      super(message);
    }
  }
}
