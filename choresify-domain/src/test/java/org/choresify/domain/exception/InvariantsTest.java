package org.choresify.domain.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class InvariantsTest {

  @Test
  void throwsWhenNull() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Invariants.requireNonNull(null, "fieldName"), InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("fieldName must not be null");
  }

  @Test
  void doesNotThrowWhenInvariantIsTrue() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Invariants.requireNonNull(new Object(), "fieldName"),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).isNull();
  }
}
