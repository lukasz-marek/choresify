package org.choresify.domain.household.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import java.util.Collections;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class HouseholdTest {
  @Test
  void cannotCreateHouseholdWithNullName() {
    // given
    var throwable =
        catchThrowableOfType(
            () ->
                Household.builder()
                    .name(null)
                    .members(Collections.singleton(HouseholdMember.of(1)))
                    .build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("name");
  }

  @Test
  void cannotCreateHouseholdWithEmptyMembers() {
    // given
    var throwable =
        catchThrowableOfType(
            () -> Household.builder().name("a name").members(Collections.emptySet()).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessage("members must not be empty");
  }

  @Test
  void cannotCreateHouseholdWithNullMembers() {
    // given
    var throwable =
        catchThrowableOfType(
            () -> Household.builder().name("a name").members(null).build(),
            InvariantViolationException.class);

    // then
    assertThat(throwable).hasMessageContaining("members");
  }

  @Test
  void cannotCreateHouseholdWithNullMember() {
    // given
    var throwable =
        catchThrowableOfType(
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
            .members(Collections.singleton(HouseholdMember.of(1)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
    assertThat(household.members()).containsExactly(HouseholdMember.of(1));
    assertThat(household.version()).isEqualTo(0);
    assertThat(household.id()).isEqualTo(0);
  }

  @Test
  void nameIsSanitized() {
    // when
    var household =
        Household.builder()
            .name("\t  \n \na  \t\t\n  name \t\t\n")
            .members(Collections.singleton(HouseholdMember.of(1)))
            .build();

    // then
    assertThat(household.name()).isEqualTo("a name");
  }
}
