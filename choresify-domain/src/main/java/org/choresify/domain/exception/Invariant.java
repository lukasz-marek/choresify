package org.choresify.domain.exception;

public final class Invariant {
  private Invariant() {}

  public static void assertTrue(boolean invariant, String message) {
    if (!invariant) {
      throw new InvariantViolationException(message);
    }
  }
}
