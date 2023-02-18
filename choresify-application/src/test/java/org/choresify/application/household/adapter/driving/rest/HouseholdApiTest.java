package org.choresify.application.household.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@IntegrationTest
class HouseholdApiTest {
  private static final String HOUSEHOLD_ENDPOINT = "/api/v1/households";

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private Members members;
  @Autowired private TransactionalRunner transactionalRunner;

  private Member createNewMember() {
    return transactionalRunner.execute(
        () ->
            members.insert(
                NewMember.builder()
                    .nickname("a new member")
                    .emailAddress("email@example.com")
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
              .members(Set.of(new HouseholdMemberDto(existingMember.id(), RoleDto.OWNER)))
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
          .containsExactly(new HouseholdMemberDto(existingMember.id(), RoleDto.OWNER));
      assertThat(createdHousehold.version()).isEqualTo(0);
    }
  }
}
