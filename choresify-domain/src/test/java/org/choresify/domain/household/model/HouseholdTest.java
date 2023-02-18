package org.choresify.domain.household.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.assertj.core.api.Assertions;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class HouseholdTest {
  @Test
  void cannotCreateHouseholdWithNullName() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Household.builder().name(null).members(Collections.emptySet()).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("name");
  }

  @Test
  void cannotCreateHouseholdWithNullMembers() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Household.builder().name("a name").members(null).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("members");
  }

  @Test
  void cannotCreateHouseholdWithNullMember() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () -> Household.builder().name("a name").members(Collections.singleton(null)).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("member");
  }

  @Test
  void canCreateHouseholdWithAllFieldsSet() {
    // when
    var household =
        Household.builder()
            .name("a name")
            .members(Collections.singleton(new HouseholdMember(1, MemberRole.MEMBER)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
    assertThat(household.members()).containsExactly(new HouseholdMember(1, MemberRole.MEMBER));
    assertThat(household.version()).isEqualTo(0);
    assertThat(household.id()).isEqualTo(0);
  }

  @Test
  void nameIsSanitized() {
    // when
    var household =
        Household.builder()
            .name("\t  \n \na  \t\t\n  name \t\t\n")
            .members(Collections.singleton(new HouseholdMember(1, MemberRole.MEMBER)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
  }
}
