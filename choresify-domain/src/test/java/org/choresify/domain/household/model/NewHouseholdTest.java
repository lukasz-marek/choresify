package org.choresify.domain.household.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class NewHouseholdTest {
  @Test
  void cannotCreateNewHouseholdWithNullName() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () ->
                NewHousehold.builder().name(null).members(Set.of(HouseholdMember.of(2137))).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("name");
  }

  @Test
  void cannotCreateNewHouseholdWithEmptyMembers() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () -> NewHousehold.builder().name("a name").members(Collections.emptySet()).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("members must not be empty");
  }

  @Test
  void cannotCreateNewHouseholdWithNullMembers() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () -> NewHousehold.builder().name("a name").members(null).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("members");
  }

  @Test
  void cannotCreateNewHouseholdWithNullMember() {
    // given
    var throwable =
        Assertions.catchThrowableOfType(
            () ->
                NewHousehold.builder().name("a name").members(Collections.singleton(null)).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("member");
  }

  @Test
  void canCreateMemberWithAllFieldsSet() {
    // when
    var household =
        NewHousehold.builder()
            .name("a name")
            .members(Collections.singleton(HouseholdMember.of(1)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
    assertThat(household.members()).containsExactly(HouseholdMember.of(1));
  }

  @Test
  void nameIsSanitized() {
    // when
    var household =
        NewHousehold.builder()
            .name("\t  \n \na  \t\t\n  name \t\t\n")
            .members(Collections.singleton(HouseholdMember.of(1)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
  }
}
