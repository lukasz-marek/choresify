package org.choresify.domain.household.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Collections;
import java.util.Set;
import org.choresify.domain.exception.ConflictingDataException;
import org.choresify.domain.exception.InvariantViolationException;
import org.choresify.domain.exception.NoSuchEntityException;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.household.port.InMemoryHouseholds;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultUpdateHouseholdUseCaseTest {
  private final Members members = new InMemoryMembers();
  private final Households households = new InMemoryHouseholds();
  private final DefaultUpdateHouseholdUseCase tested =
      new DefaultUpdateHouseholdUseCase(members, households);

  @Test
  void successfullyUpdatesWhenHouseholdExistsAndReferencedMembersExist() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().nickname("a nickname").emailAddress("email1@example.com").build());
    var existingHousehold =
        households.insert(
            NewHousehold.builder().name("a household").members(Collections.emptySet()).build());
    var forUpdate =
        existingHousehold.toBuilder()
            .name("new name")
            .members(Set.of(HouseholdMember.of(existingMember.id())))
            .build();
    // when
    var result = tested.execute(forUpdate);

    // then
    assertThat(result.id()).isEqualTo(forUpdate.id());
    assertThat(result.version()).isEqualTo(forUpdate.version() + 1);
    assertThat(result.name()).isEqualTo("new name");
    assertThat(result.members()).containsExactly(HouseholdMember.of(existingMember.id()));
  }

  @Test
  void throwsWhenHouseholdIsNull() {
    // when
    var throwable =
        catchThrowableOfType(() -> tested.execute(null), InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("household");
  }

  @Test
  void throwsWhenAnyReferencedMemberDoesNotExist() {
    // given
    var existingHousehold =
        households.insert(
            NewHousehold.builder().name("a household").members(Collections.emptySet()).build());
    var forUpdate =
        existingHousehold.toBuilder()
            .name("new name")
            .members(Set.of(HouseholdMember.of(2137)))
            .build();
    // when
    var result =
        catchThrowableOfType(() -> tested.execute(forUpdate), InvariantViolationException.class);

    // then
    assertThat(result).hasMessageContaining("Some of referenced members do not exist");
  }

  @Test
  void throwsWhenNoMembersAreReferenced() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().nickname("a nickname").emailAddress("email1@example.com").build());
    var existingHousehold =
        households.insert(
            NewHousehold.builder()
                .name("a household")
                .members(Set.of(HouseholdMember.of(existingMember.id())))
                .build());
    var forUpdate =
        existingHousehold.toBuilder().name("new name").members(Collections.emptySet()).build();
    // when
    var result =
        catchThrowableOfType(() -> tested.execute(forUpdate), InvariantViolationException.class);

    // then
    assertThat(result).hasMessageContaining("At least one member must be referenced");
  }

  @Test
  void throwsWhenHouseholdDoesNotExist() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().nickname("a nickname").emailAddress("email1@example.com").build());
    var forUpdate =
        Household.builder()
            .id(2137)
            .version(0)
            .name("new name")
            .members(Set.of(HouseholdMember.of(existingMember.id())))
            .build();
    // when
    var result = catchThrowableOfType(() -> tested.execute(forUpdate), NoSuchEntityException.class);

    // then
    assertThat(result).hasMessageContaining("Cannot update non-existent household");
  }

  @Test
  void throwsWhenOptimisticLockingFails() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().nickname("a nickname").emailAddress("email1@example.com").build());
    var existingHousehold =
        households.insert(
            NewHousehold.builder().name("a household").members(Collections.emptySet()).build());
    var forUpdate =
        existingHousehold.toBuilder()
            .name("new name")
            .version(existingHousehold.version() - 1)
            .members(Set.of(HouseholdMember.of(existingMember.id())))
            .build();
    // when
    var result =
        catchThrowableOfType(() -> tested.execute(forUpdate), ConflictingDataException.class);

    // then
    assertThat(result).hasMessageContaining("Optimistic lock failed");
  }
}
