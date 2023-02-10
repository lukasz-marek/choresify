package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.exception.DomainException.NoSuchEntityException;
import org.choresify.domain.exception.DomainException.ValidationException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultUpdateMemberUseCaseTest {
  private final Members members = new InMemoryMembers();
  private final Validator<Member> noopValidator = (member) -> {};

  private final DefaultUpdateMemberUseCase tested =
      new DefaultUpdateMemberUseCase(members, noopValidator);

  @Test
  void updateIsSuccessfulWhenMemberExists() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());

    var forUpdate =
        Member.builder()
            .id(existingMember.id())
            .nickname("a different nickname")
            .emailAddress("different@example.com")
            .build();

    // when
    var result = tested.execute(forUpdate);

    // then
    assertThat(result).isEqualTo(forUpdate);
  }

  @Test
  void updateIsRejectedWhenMemberDoesNotExist() {
    // given
    var forUpdate =
        Member.builder()
            .id(2137)
            .nickname("a different nickname")
            .emailAddress("different@example.com")
            .build();

    // when
    var result = catchThrowableOfType(() -> tested.execute(forUpdate), NoSuchEntityException.class);

    // then
    assertThat(result).hasMessageContaining("Cannot update non-existent member");
  }

  @Test
  void updateIsRejectedWhenValidationFails() {
    // given
    var tested =
        new DefaultUpdateMemberUseCase(
            members,
            (member) -> {
              throw new ValidationException("validation failed");
            });
    var forUpdate =
        Member.builder()
            .id(2137)
            .nickname("a different nickname")
            .emailAddress("different@example.com")
            .build();

    // when
    var result = catchThrowableOfType(() -> tested.execute(forUpdate), ValidationException.class);

    // then
    assertThat(result).hasMessageContaining("validation failed");
  }
}
