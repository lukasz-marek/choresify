package org.choresify.application.common;

import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ResponseStatusException.class)
  ErrorResponse handleResponseStatusException(ResponseStatusException responseStatusException) {
    log.info("Handling exception", responseStatusException);
    return ErrorResponse.builder(
            responseStatusException,
            responseStatusException.getStatusCode(),
            responseStatusException.getMessage())
        .title("Something went wrong")
        .property("reason", StringUtils.defaultString(responseStatusException.getReason()))
        .property("timestamp", Instant.now())
        .build();
  }
}
