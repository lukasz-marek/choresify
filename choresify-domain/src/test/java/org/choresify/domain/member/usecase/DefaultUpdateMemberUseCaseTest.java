package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.choresify.domain.exception.ConflictingDataException;
import org.choresify.domain.exception.InvariantViolationException;
import org.choresify.domain.exception.NoSuchEntityException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultUpdateMemberUseCaseTest {
  private final Members members = new InMemoryMembers();

  private final DefaultUpdateMemberUseCase tested = new DefaultUpdateMemberUseCase(members);

  private static Member incrementVersion(Member forUpdate) {
    return forUpdate.toBuilder().version(forUpdate.version() + 1).build();
  }

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
    assertThat(result).isEqualTo(incrementVersion(forUpdate));
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
  void updateIsRejectedWhenEmailIsChangedToEmailInUseByDifferentMember() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());
    members.insert(
        NewMember.builder().emailAddress("different@example.com").nickname("a nickname").build());

    var forUpdate =
        Member.builder()
            .id(existingMember.id())
            .nickname("a different nickname")
            .emailAddress("different@example.com")
            .build();

    // when
    var result =
        catchThrowableOfType(() -> tested.execute(forUpdate), ConflictingDataException.class);

    // then
    assertThat(result).hasMessageContaining("Email address already in use by a different member");
  }

  @Test
  void updateIsSuccessfulWhenEmailIsChangedToEmailNotInUse() {
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
    assertThat(result).isEqualTo(incrementVersion(forUpdate));
  }

  @Test
  void throwsWhenOptimisticLockingFails() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());

    var forUpdate = existingMember.toBuilder().version(existingMember.version() - 1).build();

    // when
    var result =
        catchThrowableOfType(() -> tested.execute(forUpdate), ConflictingDataException.class);

    // then
    assertThat(result).hasMessageContaining("Optimistic lock failed");
  }

  @Test
  void throwsWhenMemberIsNull() {
    // when
    var result =
        catchThrowableOfType(() -> tested.execute(null), InvariantViolationException.class);

    // then
    assertThat(result.getMessage()).isEqualTo("member must not be null");
  }
}
