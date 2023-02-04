package org.choresify.application.member.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@IntegrationTest
public class MemberApiTest {
  private static final String MEMBER_ENDPOINT = "/api/v1/members";

  @Autowired private TestRestTemplate testRestTemplate;

  @Test
  void createsNewMemberWhenPayloadIsValid() {
    // given
    var newMember =
        NewMemberDto.builder()
            .nickname("Doctor Strange")
            .emailAddress("doctor@strange.com")
            .build();

    // when
    var response = testRestTemplate.postForEntity(MEMBER_ENDPOINT, newMember, MemberDto.class);

    // then
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    var createdMember = response.getBody();
    assertThat(createdMember).isNotNull();
    assertThat(createdMember.getNickname()).isEqualTo("Doctor Strange");
    assertThat(createdMember.getEmailAddress()).isEqualTo("doctor@strange.com");
    assertThat(createdMember.getId()).isPositive();
    assertThat(createdMember.getVersion()).isEqualTo(0L);
  }
}