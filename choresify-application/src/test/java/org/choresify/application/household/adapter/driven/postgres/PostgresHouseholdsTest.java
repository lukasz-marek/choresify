package org.choresify.application.household.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import java.util.Set;
import java.util.UUID;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
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
}
