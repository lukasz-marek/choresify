package org.choresify.domain.common.sanitation;

import java.util.Optional;

public final class NameSanitizer {
  private NameSanitizer() {}

  public static String sanitize(String name) {
    return Optional.ofNullable(name)
        .map(String::trim)
        .map(aName -> aName.replaceAll("\\s+", " "))
        .orElse(null);
  }
}
