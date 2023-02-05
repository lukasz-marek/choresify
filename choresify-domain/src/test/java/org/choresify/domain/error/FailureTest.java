package org.choresify.domain.error;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class FailureTest {

  @Test
  void constructsNewFailureForOneMessage() {
    // when
    var failure = Failure.of(Category.PRECONDITION, "an error has occurred");

    // then
    assertThat(failure).isNotNull();
    assertThat(failure.getFailureDetails())
        .containsExactly(FailureDetails.of(Category.PRECONDITION, "an error has occurred"));
  }

  @Test
  void constructsNewFailureForMultipleMessages() {
    // when
    var failure = Failure.of(Category.VALIDATION, List.of("error 1", "error 2"));

    // then
    assertThat(failure).isNotNull();
    assertThat(failure.getFailureDetails())
        .containsExactlyInAnyOrder(
            FailureDetails.of(Category.VALIDATION, "error 1"),
            FailureDetails.of(Category.VALIDATION, "error 2"));
  }
}
