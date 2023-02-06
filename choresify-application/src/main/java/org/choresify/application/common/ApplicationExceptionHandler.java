package org.choresify.application.common;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.choresify.domain.exception.DomainException.PreconditionFailedException;
import org.choresify.domain.exception.DomainException.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  ErrorResponse handleResponseStatusException(ResponseStatusException exception) {
    log.info("Handling exception", exception);
    return ErrorResponse.builder(exception, exception.getStatusCode(), exception.getMessage())
        .title("Something went wrong")
        .property("reason", StringUtils.defaultString(exception.getReason()))
        .property("timestamp", Instant.now())
        .build();
  }

  @ExceptionHandler(ValidationException.class)
  ErrorResponse handleDomainValidationException(ValidationException exception) {
    log.info("Handling exception", exception);
    return ErrorResponse.builder(exception, HttpStatus.BAD_REQUEST, exception.getMessage())
        .title("Something went wrong")
        .property("timestamp", Instant.now())
        .build();
  }

  @ExceptionHandler(PreconditionFailedException.class)
  ErrorResponse handlePreconditionFailedException(PreconditionFailedException exception) {
    log.info("Handling exception", exception);
    return ErrorResponse.builder(exception, HttpStatus.CONFLICT, exception.getMessage())
        .title("Something went wrong")
        .property("timestamp", Instant.now())
        .build();
  }
}
