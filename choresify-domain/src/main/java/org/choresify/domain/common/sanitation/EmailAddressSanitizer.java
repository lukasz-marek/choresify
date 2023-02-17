package org.choresify.domain.common.sanitation;

import java.util.Optional;

public final class EmailAddressSanitizer {
  private EmailAddressSanitizer() {}

  public static String sanitize(String emailAddress) {
    return Optional.ofNullable(emailAddress)
        .map(String::toLowerCase)
        .map(String::trim)
        .map(email -> email.replaceAll("\\s", ""))
        .orElse(null);
  }
}
