package org.choresify.domain.common.sanitation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NameSanitizerTest {

  @Test
  void returnsNullOnNull() {
    // when
    var result = NameSanitizer.sanitize(null);

    // then
    assertThat(result).isNull();
  }

  @Test
  void nameIsTrimmed() {
    // when
    var result = NameSanitizer.sanitize("  Albus Dumbledore    ");

    // then
    assertThat(result).isEqualTo("Albus Dumbledore");
  }

  @Test
  void consecutiveWhiteSpaceAreTurnedIntoOneSpace() {
    // when
    var result = NameSanitizer.sanitize("\t\n\t\nAlbus\n\t\t    \t\nDumbledore\t\t\t");

    // then
    assertThat(result).isEqualTo("Albus Dumbledore");
  }
}
