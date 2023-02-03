package org.choresify.domain.common.validation;

import org.choresify.domain.common.validation.DomainException.DomainValidationException;

public final class Guards {
  private Guards() {}

  public static <T> T validateNonNull(T object, String objectName) {
    if (object == null)
      throw new DomainValidationException("\"%s\" must not be null".formatted(objectName));
    return object;
  }
}
