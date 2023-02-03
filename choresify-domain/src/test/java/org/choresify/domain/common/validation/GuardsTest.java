package org.choresify.domain.common.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.choresify.domain.common.validation.DomainException.DomainValidationException;
import org.junit.jupiter.api.Test;

class GuardsTest {

  @Test
  void throwsOnNull() {
    var exception =
        catchThrowableOfType(
            () -> Guards.validateNonNull(null, "test object"), DomainValidationException.class);
    assertThat(exception).hasMessage("\"test object\" must not be null");
  }

  @Test
  void returnsProvidedValueWhenNotNull() {
    // given
    var nonNullValue = new Object();

    // when
    var result = Guards.validateNonNull(nonNullValue, "an object");

    // then
    assertThat(result).isSameAs(nonNullValue);
  }
}
