package org.choresify.domain.common.validation;

public sealed class DomainException extends RuntimeException {
  protected DomainException(String message) {
    super(message);
  }

  public static final class DomainValidationException extends DomainException {
    DomainValidationException(String message) {
      super(message);
    }
  }
}
