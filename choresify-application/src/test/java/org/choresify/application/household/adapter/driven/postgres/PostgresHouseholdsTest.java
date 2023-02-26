package org.choresify.application.household.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchRuntimeException;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.PersistenceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@PersistenceTest
class PostgresHouseholdsTest {
  @Autowired PostgresHouseholds tested;
  @Autowired Members members;

  private Member createMember() {
    return members.insert(
        NewMember.builder()
            .emailAddress(UUID.randomUUID() + "@example.com")
            .nickname(UUID.randomUUID().toString())
            .build());
  }

  @Nested
  class Insertion {
    @Test
    void insertsHouseholdWhenReferencedMembersExist() {
      // given
      var existingMember = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember.id())))
              .build();

      // when
      var household = tested.insert(newHousehold);

      // then
      assertThat(household.version()).isEqualTo(0);
      assertThat(household.name()).isEqualTo("a household");
      assertThat(household.members())
          .containsExactlyInAnyOrder(new HouseholdMember(existingMember.id()));
    }

    @Test
    void failsToCreateHouseholdWhenReferencedMemberDoesNotExist() {
      // given
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(-2)))
              .build();

      // when
      var throwable = catchThrowable(() -> tested.insert(newHousehold));

      // then
      assertThat(throwable).isNotNull();
    }
  }

  @Nested
  class GetById {

    @Test
    void canFetchExistingHousehold() {
      // given
      var existingMember = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember.id())))
              .build();
      var existingId = tested.insert(newHousehold).id();
      // when
      var maybeHousehold = tested.getById(existingId);

      // then
      assertThat(maybeHousehold).isPresent();
      var household = maybeHousehold.get();
      assertThat(household.version()).isEqualTo(0);
      assertThat(household.name()).isEqualTo("a household");
      assertThat(household.members())
          .containsExactlyInAnyOrder(new HouseholdMember(existingMember.id()));
    }

    @Test
    void returnsEmptyWhenHouseholdIsNotExistent() {
      // when
      var maybeHousehold = tested.getById(2137L);

      // then
      assertThat(maybeHousehold).isEmpty();
    }
  }

  @Nested
  class Update {
    @Test
    void updateIsSuccessfulWhenHouseholdExistsAndVersionMatches() {
      // given
      var existingMember = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember.id())))
              .build();
      var exitingHousehold = tested.insert(newHousehold);
      var forUpdate =
          exitingHousehold.toBuilder().name("a new name").members(Collections.emptySet()).build();

      // when
      var maybeHousehold = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(maybeHousehold).isPresent();
      var household = maybeHousehold.get();
      assertThat(household.version()).isEqualTo(exitingHousehold.version() + 1);
      assertThat(household.name()).isEqualTo("a new name");
      assertThat(household.members()).isEmpty();
    }

    @Test
    void updateIsSuccessfulWhenAllReferencedMembersExistAndHouseholdExistsAndVersionMatches() {
      // given
      var existingMember1 = createMember();
      var existingMember2 = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember1.id())))
              .build();
      var exitingHousehold = tested.insert(newHousehold);
      var forUpdate =
          exitingHousehold.toBuilder()
              .members(
                  Set.of(
                      new HouseholdMember(existingMember1.id()),
                      new HouseholdMember(existingMember2.id())))
              .build();

      // when
      var maybeHousehold = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(maybeHousehold).isPresent();
      var household = maybeHousehold.get();
      assertThat(household.version()).isEqualTo(exitingHousehold.version() + 1);
      assertThat(household.name()).isEqualTo("a household");
      assertThat(household.members())
          .containsExactlyInAnyOrder(
              new HouseholdMember(existingMember1.id()), new HouseholdMember(existingMember2.id()));
    }

    @Test
    void updateFailsWhenNonExistentMemberIsReferenced() {
      // given
      var existingMember1 = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember1.id())))
              .build();
      var exitingHousehold = tested.insert(newHousehold);
      var forUpdate =
          exitingHousehold.toBuilder()
              .members(
                  Set.of(new HouseholdMember(existingMember1.id()), new HouseholdMember(2137L)))
              .build();

      // when
      var throwable = catchRuntimeException(() -> tested.updateWithOptimisticLock(forUpdate));

      // then
      assertThat(throwable).isNotNull();
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 1})
    void updateFailsWhenVersionsDoNotMatch(long versionDiff) {
      // given
      var existingMember1 = createMember();
      var existingMember2 = createMember();
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(existingMember1.id())))
              .build();
      var exitingHousehold = tested.insert(newHousehold);
      var forUpdate =
          exitingHousehold.toBuilder()
              .version(exitingHousehold.version() - versionDiff)
              .members(
                  Set.of(
                      new HouseholdMember(existingMember1.id()),
                      new HouseholdMember(existingMember2.id())))
              .build();

      // when
      var maybeHousehold = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(maybeHousehold).isEmpty();
    }

    @Test
    void updateFailsWhenHouseHoldDoesNotExist() {
      // given
      var forUpdate =
          Household.builder()
              .id(2137)
              .version(1)
              .members(Collections.emptySet())
              .name("a household")
              .build();

      // when
      var maybeHousehold = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(maybeHousehold).isEmpty();
    }
  }
}
