package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.domain.error.Category;
import org.choresify.domain.error.FailureDetails;
import org.choresify.domain.member.model.NewMember;
import org.junit.jupiter.api.Test;

class NewMemberValidatorTest {
  private final NewMemberValidator tested = new NewMemberValidator();

  @Test
  void returnsErrorWhenNewMemberIsNull() {
    // when
    var result = tested.validate(null);

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError().getFailureDetails())
        .containsExactly(FailureDetails.of(Category.VALIDATION, "NewMember must not be null"));
  }

  @Test
  void returnsErrorWhenNicknameIsNull() {
    // given
    var member = NewMember.builder().nickname(null).emailAddress("email@example.com").build();

    // when
    var result = tested.validate(member);

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError().getFailureDetails())
        .containsExactly(FailureDetails.of(Category.VALIDATION, "nickname must not be null"));
  }

  @Test
  void returnsErrorWhenEmailAddressIsNull() {
    // given
    var member = NewMember.builder().nickname("a nickname").emailAddress(null).build();

    // when
    var result = tested.validate(member);

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError().getFailureDetails())
        .containsExactly(FailureDetails.of(Category.VALIDATION, "email address must not be null"));
  }

  @Test
  void returnsErrorWhenEmailAddressIsNullAndNicknameIsNull() {
    // given
    var member = NewMember.builder().nickname(null).emailAddress(null).build();

    // when
    var result = tested.validate(member);

    // then
    assertThat(result.isInvalid()).isTrue();
    assertThat(result.getError().getFailureDetails())
        .containsExactlyInAnyOrder(
            FailureDetails.of(Category.VALIDATION, "email address must not be null"),
            FailureDetails.of(Category.VALIDATION, "nickname must not be null"));
  }
}
