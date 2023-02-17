package org.choresify.domain.member.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class MemberTest {
  @Test
  void cannotCreateMemberWithNullNickname() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Member.builder().nickname(null).emailAddress("email@example.com").build(),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("nickname must not be null");
  }

  @Test
  void cannotCreateMemberWithNullEmail() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Member.builder().nickname("nickname").emailAddress(null).build(),
            InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("emailAddress must not be null");
  }

  @Test
  void canCreateMemberWithAllRequiredFields() {
    // when
    var member = Member.builder().nickname("a nickname").emailAddress("email@example.com").build();

    // then
    assertThat(member.nickname()).isEqualTo("a nickname");
    assertThat(member.emailAddress()).isEqualTo("email@example.com");
  }

  @Test
  void emailAddressIsNormalized() {
    // given
    var member =
        Member.builder().nickname("a nickname").emailAddress(" EmAil@ exa mple.coM  ").build();

    // when
    var email = member.emailAddress();

    // then
    assertThat(email).isEqualTo("email@example.com");
  }

  @Test
  void nicknameIsNormalized() {
    // given
    var member =
        Member.builder().nickname("  a   nickname  \n\t").emailAddress("email@example.com").build();

    // when
    var nickname = member.nickname();

    // then
    assertThat(nickname).isEqualTo("a nickname");
  }
}
