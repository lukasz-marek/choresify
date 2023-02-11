package org.choresify.domain.exception;

public final class ConflictingDataException extends IllegalArgumentException {

  public ConflictingDataException(String message) {
    super(message);
  }
}
