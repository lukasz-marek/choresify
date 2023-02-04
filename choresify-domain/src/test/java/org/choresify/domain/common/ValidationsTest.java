package org.choresify.domain.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.vavr.control.Validation;
import org.choresify.domain.common.validation.Validations;
import org.junit.jupiter.api.Test;

class ValidationsTest {

  @Test
  void returnsValidWhenAllValidationsAreValid() {
    // given
    var validated = new Object();

    // when
    var result =
        Validations.joining(validated, (object) -> Validation.valid("this value is ignored"));

    // then
    assertThat(result.isValid()).isTrue();
    assertThat(result.get()).isSameAs(validated);
  }

  @Test
  void returnsValidWhenValidationsAreEmpty() {
    // given
    var validated = new Object();

    // when
    var result = Validations.joining(validated);

    // then
    assertThat(result.isValid()).isTrue();
    assertThat(result.get()).isSameAs(validated);
  }

  @Test
  void returnsInvalidWhenAllValidationsAreInvalid() {
    // given
    var validated = new Object();

    // when
    var result =
        Validations.joining(
            validated,
            (object) -> Validation.invalid("error 1"),
            (object) -> Validation.invalid("error 2"));

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError()).containsExactlyInAnyOrder("error 1", "error 2");
  }

  @Test
  void returnsInvalidWhenAnyValidationsAreInvalid() {
    // given
    var validated = new Object();

    // when
    var result =
        Validations.joining(
            validated,
            (object) -> Validation.valid("not an error"),
            (object) -> Validation.invalid("error 1"),
            (object) -> Validation.valid("not an error as well"));

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError()).containsExactlyInAnyOrder("error 1");
  }
}
