package org.choresify.domain.member.model;

import org.assertj.core.api.Assertions;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class NewMemberTest {

  @Test
  void cannotCreateNewMemberWithNullNickname() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> NewMember.builder().nickname(null).emailAddress("email@example.com").build(),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("nickname must not be null");
  }

  @Test
  void cannotCreateNewMemberWithNullEmail() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> NewMember.builder().nickname("nickname").emailAddress(null).build(),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("emailAddress must not be null");
  }
}
