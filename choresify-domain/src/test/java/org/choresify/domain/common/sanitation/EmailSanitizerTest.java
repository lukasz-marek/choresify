package org.choresify.domain.common.sanitation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class EmailSanitizerTest {

  @Test
  void returnsNullOnNull() {
    // when
    var result = EmailAddressSanitizer.sanitize(null);

    // then
    assertThat(result).isNull();
  }

  @Test
  void turnsEmailIntoLowerCase() {
    // when
    var result = EmailAddressSanitizer.sanitize("EmAiL@eXAMple.COm");

    // then
    assertThat(result).isEqualTo("email@example.com");
  }

  @Test
  void trimsWhiteSpace() {
    // when
    var result = EmailAddressSanitizer.sanitize("   email@example.com   ");

    // then
    assertThat(result).isEqualTo("email@example.com");
  }

  @Test
  void removesWhiteSpaceFromInside() {
    // when
    var result = EmailAddressSanitizer.sanitize("\tem  ail@\n  exam  ple.  com");

    // then
    assertThat(result).isEqualTo("email@example.com");
  }
}
