package org.choresify.domain.exception;

import edu.umd.cs.findbugs.annotations.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Invariants {
  private Invariants() {}

  public static void requireNonNull(@Nullable Object value, String fieldName) {
    if (value == null) {
      log.info("Invariant failed - [{}] is null", fieldName);
      throw new InvariantViolationException("%s must not be null".formatted(fieldName));
    }
  }

  public static void requireTrue(boolean requirement, String errorMessage) {
    if (!requirement) {
      log.info("Invariant failed - [{}]", errorMessage);
      throw new InvariantViolationException(errorMessage);
    }
  }
}
