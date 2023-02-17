package org.choresify.domain.member.model;

import static org.assertj.core.api.Assertions.assertThat;

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
    assertThat(throwable).hasMessageContaining("nickname must not be null");
  }

  @Test
  void cannotCreateNewMemberWithNullEmail() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> NewMember.builder().nickname("nickname").emailAddress(null).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("emailAddress must not be null");
  }

  @Test
  void canCreateMemberWithAllRequiredFields() {
    // when
    var newMember =
        NewMember.builder().nickname("a nickname").emailAddress("email@example.com").build();

    // then
    assertThat(newMember.nickname()).isEqualTo("a nickname");
    assertThat(newMember.emailAddress()).isEqualTo("email@example.com");
  }

  @Test
  void emailAddressIsNormalized() {
    // given
    var newMember =
        NewMember.builder().nickname("a nickname").emailAddress(" EmAil@ exa mple.coM  ").build();

    // when
    var email = newMember.emailAddress();

    // then
    assertThat(email).isEqualTo("email@example.com");
  }

  @Test
  void nicknameIsNormalized() {
    // given
    var newMember =
        NewMember.builder()
            .nickname("  a   nickname  \n\t")
            .emailAddress("email@example.com")
            .build();

    // when
    var nickname = newMember.nickname();

    // then
    assertThat(nickname).isEqualTo("a nickname");
  }
}
