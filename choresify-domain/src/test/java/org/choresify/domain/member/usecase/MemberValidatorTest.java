package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.choresify.domain.exception.DomainException;
import org.choresify.domain.member.model.Member;
import org.junit.jupiter.api.Test;

class MemberValidatorTest {
  private final MemberValidator tested = new MemberValidator();

  @Test
  void throwsExceptionWhenNewMemberIsNull() {
    // when
    var result =
        catchThrowableOfType(
            () -> tested.validate(null), DomainException.ValidationException.class);

    // then
    assertThat(result).hasMessage("NewMember must not be null");
  }

  @Test
  void throwsExceptionWhenNicknameIsNull() {
    // given
    var member = Member.builder().nickname(null).emailAddress("email@example.com").build();

    // when
    var result =
        catchThrowableOfType(
            () -> tested.validate(member), DomainException.ValidationException.class);

    // then
    assertThat(result).hasMessage("nickname must not be null");
  }

  @Test
  void throwsExceptionWhenEmailAddressIsNull() {
    // given
    var member = Member.builder().nickname("a nickname").emailAddress(null).build();

    // when
    var result =
        catchThrowableOfType(
            () -> tested.validate(member), DomainException.ValidationException.class);

    // then
    assertThat(result).hasMessage("email address must not be null");
  }

  @Test
  void doesNotThrowOnValidInput() {
    // given
    var member = Member.builder().nickname("a nickname").emailAddress("email@example.com").build();

    // when
    var result =
        catchThrowableOfType(
            () -> tested.validate(member), DomainException.ValidationException.class);

    // then
    assertThat(result).isNull();
  }
}
