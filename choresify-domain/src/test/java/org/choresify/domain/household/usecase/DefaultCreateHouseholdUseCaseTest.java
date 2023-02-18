package org.choresify.domain.household.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Collections;
import java.util.Set;
import org.choresify.domain.exception.InvariantViolationException;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.household.port.InMemoryHouseholds;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultCreateHouseholdUseCaseTest {
  private final Members members = new InMemoryMembers();
  private final Households households = new InMemoryHouseholds();
  private final DefaultCreateHouseholdUseCase tested =
      new DefaultCreateHouseholdUseCase(members, households);

  @Test
  void throwsWhenHouseholdIsNull() {
    // when
    var throwable =
        catchThrowableOfType(() -> tested.execute(null), InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("new household");
  }

  @Test
  void failsToCreateHouseholdWhenAnyMemberIsMissing() {
    // given
    var member1 =
        members.insert(
            NewMember.builder().emailAddress("email1@example.com").nickname("a nickname").build());
    var member2 =
        members.insert(
            NewMember.builder().emailAddress("email2@example.com").nickname("a nickname").build());
    var newHousehold =
        NewHousehold.builder()
            .name("household name")
            .members(
                Set.of(
                    new HouseholdMember(member1.id()),
                    new HouseholdMember(member2.id()),
                    new HouseholdMember(2137L)))
            .build();

    // when
    var throwable =
        catchThrowableOfType(() -> tested.execute(newHousehold), InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessage("Some of referenced members do not exist");
  }

  @Test
  void failsToCreateHouseholdWhenNoMembersAreReferenced() {
    // given
    var newHousehold =
        NewHousehold.builder().name("household name").members(Collections.emptySet()).build();

    // when
    var throwable =
        catchThrowableOfType(() -> tested.execute(newHousehold), InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessage("At least one member must be referenced");
  }

  @Test
  void createsHousehold() {
    // given
    var member1 =
        members.insert(
            NewMember.builder().emailAddress("email1@example.com").nickname("a nickname").build());
    var member2 =
        members.insert(
            NewMember.builder().emailAddress("email2@example.com").nickname("a nickname").build());
    var newHousehold =
        NewHousehold.builder()
            .name("household name")
            .members(Set.of(new HouseholdMember(member1.id()), new HouseholdMember(member2.id())))
            .build();

    // when
    var household = tested.execute(newHousehold);

    // then
    assertThat(household.version()).isEqualTo(0);
    assertThat(household.name()).isEqualTo("household name");
    assertThat(household.members())
        .containsExactlyInAnyOrder(
            new HouseholdMember(member1.id()), new HouseholdMember(member2.id()));
  }
}
