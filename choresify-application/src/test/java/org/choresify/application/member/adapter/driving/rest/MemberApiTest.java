package org.choresify.application.member.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

@IntegrationTest
class MemberApiTest {
  private static final String MEMBER_ENDPOINT = "/api/v1/members";

  @Autowired private TestRestTemplate testRestTemplate;

  @Nested
  class Create {

    static Stream<Arguments> incompletePayloads() {
      return Stream.of(
          Arguments.of(
              NewMemberDto.builder().nickname(null).emailAddress("doctor@strange.com").build(),
              List.of("nickname")),
          Arguments.of(
              NewMemberDto.builder().nickname("doctor strange").emailAddress(null).build(),
              List.of("email address")),
          Arguments.of(
              NewMemberDto.builder().nickname(null).emailAddress(null).build(),
              List.of("nickname", "email address")));
    }

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

    @Test
    void failsToCreateTwoMembersWithTheSameEmail() {
      // given
      var newMember1 =
          NewMemberDto.builder()
              .nickname("Doctor Strange")
              .emailAddress("doctor@strange.com")
              .build();
      var newMember2 =
          NewMemberDto.builder()
              .nickname("Somebody else")
              .emailAddress("doctor@strange.com")
              .build();

      // when
      var successfulResponse =
          testRestTemplate.postForEntity(MEMBER_ENDPOINT, newMember1, MemberDto.class);
      var failedResponse =
          testRestTemplate.postForEntity(MEMBER_ENDPOINT, newMember2, MemberDto.class);

      // then
      assertThat(successfulResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(failedResponse.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @ParameterizedTest
    @MethodSource("incompletePayloads")
    void failsToCreateMemberWhenPayloadIsIncomplete(
        NewMemberDto incompleteNewMember, List<String> missingFieldNames) {
      // when
      var response =
          testRestTemplate.postForEntity(MEMBER_ENDPOINT, incompleteNewMember, String.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      var responseBody = response.getBody();
      assertThat(responseBody).contains(missingFieldNames);
    }
  }
}
