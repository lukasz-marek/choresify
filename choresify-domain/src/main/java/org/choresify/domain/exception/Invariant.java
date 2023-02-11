package org.choresify.domain.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class Invariant {
  private Invariant() {}

  public static void assertTrue(boolean invariant, String message) {
    if (!invariant) {
      log.debug("Invariant violation detected: {}", message);
      throw new InvariantViolationException(message);
    }
  }
}
