package org.choresify.domain.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class RequirementTest {

  @Test
  void throwsWhenInvariantIsFalse() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Invariant.assertTrue(false, "failure message"),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("failure message");
  }

  @Test
  void doesNotThrowWhenInvariantIsTrue() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Invariant.assertTrue(true, "failure message"), InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).isNull();
  }
}
