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

  public static final class ConflictingDataException extends DomainException {
    public ConflictingDataException(String message) {
      super(message);
    }
  }

  public static final class NoSuchEntityException extends DomainException {
    public NoSuchEntityException(String message) {
      super(message);
    }
  }
}
