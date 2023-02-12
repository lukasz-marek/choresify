package org.choresify.domain.exception;

import edu.umd.cs.findbugs.annotations.Nullable;

public final class Invariants {
  private Invariants() {}

  public static void requireNonNull(@Nullable Object value, String fieldName) {
    if (value == null) {
      throw new InvariantViolationException("%s must not be null".formatted(fieldName));
    }
  }
}
