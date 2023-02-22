package org.choresify.application.household.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.UUID;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

@IntegrationTest
class HouseholdApiTest {
  private static final String HOUSEHOLD_ENDPOINT = "/api/v1/households";

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private Members members;
  @Autowired private TransactionalRunner transactionalRunner;
  @Autowired private Households households;

  private Household insert(NewHousehold newHousehold) {
    return transactionalRunner.execute(() -> households.insert(newHousehold));
  }

  private Member createNewMember() {
    return transactionalRunner.execute(
        () ->
            members.insert(
                NewMember.builder()
                    .nickname("a new member")
                    .emailAddress(UUID.randomUUID() + "@example.com")
                    .build()));
  }

  @Nested
  class Create {
    @Test
    void createsHouseholdForExistingMember() {
      // given
      var existingMember = createNewMember();
      var dto =
          NewHouseholdDto.builder()
              .name("Bag End")
              .members(Set.of(new HouseholdMemberDto(existingMember.id())))
              .build();

      // when
      var response = testRestTemplate.postForEntity(HOUSEHOLD_ENDPOINT, dto, HouseholdDto.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      var createdHousehold = response.getBody();
      assertThat(createdHousehold).isNotNull();
      assertThat(createdHousehold.id()).isPositive();
      assertThat(createdHousehold.name()).isEqualTo("Bag End");
      assertThat(createdHousehold.members())
          .containsExactly(new HouseholdMemberDto(existingMember.id()));
      assertThat(createdHousehold.version()).isEqualTo(0);
    }

    @Test
    void failsToCreateHouseholdWhenAnyMemberDoesNotExist() {
      // given
      var existingMember = createNewMember();
      var dto =
          NewHouseholdDto.builder()
              .name("Bag End")
              .members(
                  Set.of(new HouseholdMemberDto(existingMember.id()), new HouseholdMemberDto(2137)))
              .build();

      // when
      var response = testRestTemplate.postForEntity(HOUSEHOLD_ENDPOINT, dto, String.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
  }

  @Nested
  class Read {
    @Test
    void canReadExistingHousehold() {
      // given
      var existingMember = createNewMember();
      var existingHousehold =
          insert(
              NewHousehold.builder()
                  .name("a household")
                  .members(Set.of(new HouseholdMember(existingMember.id())))
                  .build());

      // when
      var response =
          testRestTemplate.getForEntity(
              HOUSEHOLD_ENDPOINT + "/" + existingHousehold.id(), HouseholdDto.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      var responseBody = response.getBody();
      assertThat(responseBody).isNotNull();
      assertThat(responseBody.version()).isNotNegative();
      assertThat(responseBody.name()).isEqualTo("a household");
      assertThat(responseBody.id()).isEqualTo(existingHousehold.id());
      assertThat(responseBody.members())
          .containsExactly(new HouseholdMemberDto(existingMember.id()));
    }

    @Test
    void returnsNotFoundWhenHouseholdIsNotPresent() {
      // when
      var response = testRestTemplate.getForEntity(HOUSEHOLD_ENDPOINT + "/" + 2137, String.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
  }

  @Nested
  class Update {
    @Test
    void canUpdateWhenIdAndVersionMatch() {
      // given
      var member1 = createNewMember();
      var member2 = createNewMember();
      var existingHousehold =
          insert(
              NewHousehold.builder()
                  .name("a household")
                  .members(Set.of(new HouseholdMember(member1.id())))
                  .build());
      var forUpdate =
          HouseholdDto.builder()
              .id(existingHousehold.id())
              .version(existingHousehold.version())
              .name("a new name")
              .members(Set.of(new HouseholdMemberDto(member2.id())))
              .build();

      // when
      var response =
          testRestTemplate.exchange(
              RequestEntity.put(HOUSEHOLD_ENDPOINT + "/{householdId}", existingHousehold.id())
                  .body(forUpdate),
              HouseholdDto.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      var updatedHousehold = response.getBody();
      assertThat(updatedHousehold).isNotNull();
      assertThat(updatedHousehold.id()).isEqualTo(forUpdate.id());
      assertThat(updatedHousehold.version()).isEqualTo(forUpdate.version() + 1);
      assertThat(updatedHousehold.members()).containsExactly(new HouseholdMemberDto(member2.id()));
      assertThat(updatedHousehold.name()).isEqualTo(forUpdate.name());
    }
  }
}
