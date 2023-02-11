package org.choresify.domain.exception;

public final class ConflictingDataException extends RuntimeException {

  public ConflictingDataException(String message) {
    super(message);
  }
}
