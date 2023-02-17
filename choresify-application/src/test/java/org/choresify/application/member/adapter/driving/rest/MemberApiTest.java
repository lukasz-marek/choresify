package org.choresify.application.member.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

@IntegrationTest
class MemberApiTest {
  private static final String MEMBER_ENDPOINT = "/api/v1/members";

  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private Members members;
  @Autowired private TransactionalRunner transactionalRunner;

  private Member insert(NewMember newMember) {
    return transactionalRunner.execute(() -> members.insert(newMember));
  }

  @Nested
  class Create {

    static Stream<Arguments> incompleteNewMemberPayloads() {
      return Stream.of(
          Arguments.of(
              NewMemberDto.builder().nickname(null).emailAddress("doctor@strange.com").build(),
              List.of("nickname")),
          Arguments.of(
              NewMemberDto.builder().nickname("doctor strange").emailAddress(null).build(),
              List.of("emailAddress")));
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
      assertThat(createdMember.nickname()).isEqualTo("Doctor Strange");
      assertThat(createdMember.emailAddress()).isEqualTo("doctor@strange.com");
      assertThat(createdMember.id()).isPositive();
      assertThat(createdMember.version()).isEqualTo(0);
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
    @MethodSource("incompleteNewMemberPayloads")
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

  @Nested
  class Read {
    @Test
    void getReturnsMemberWhenItExists() {
      // given
      var existingMember =
          insert(
              NewMember.builder()
                  .nickname("a new member")
                  .emailAddress("member@example.com")
                  .build());
      // when
      var response =
          testRestTemplate.getForEntity(
              MEMBER_ENDPOINT + "/" + existingMember.id(), MemberDto.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
      var memberFromApi = response.getBody();
      assertThat(memberFromApi).isNotNull();
      assertThat(memberFromApi.nickname()).isEqualTo(existingMember.nickname());
      assertThat(memberFromApi.emailAddress()).isEqualTo(existingMember.emailAddress());
      assertThat(memberFromApi.id()).isEqualTo(existingMember.id());
    }

    @Test
    void getReturnsNotFoundWhenNoMemberExists() {
      // when
      var response = testRestTemplate.getForEntity(MEMBER_ENDPOINT + "/2137", MemberDto.class);

      // then
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      var memberFromApi = response.getBody();
      assertThat(memberFromApi).isNull();
    }
  }

  @Nested
  class Update {
    static Stream<Arguments> incompleteMemberPayloads() {
      return Stream.of(
          Arguments.of(
              MemberDto.builder()
                  .nickname(null)
                  .emailAddress("doctor@strange.com")
                  .id(2137L)
                  .build(),
              List.of("nickname")),
          Arguments.of(
              MemberDto.builder().nickname("doctor strange").emailAddress(null).id(2137L).build(),
              List.of("emailAddress")));
    }

    @Test
    void updateIsSuccessfulWhenMemberExists() {
      // given
      var existingMember =
          insert(NewMember.builder().nickname("Alan").emailAddress("alan@kay.com").build());
      var updateInput =
          MemberDto.builder()
              .nickname("John")
              .emailAddress("john@mccarthy.com")
              .id(existingMember.id())
              .build();

      // when
      var updated =
          testRestTemplate.exchange(
              RequestEntity.put(MEMBER_ENDPOINT + "/{memberId}", existingMember.id())
                  .body(updateInput),
              MemberDto.class);

      // then
      assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.OK);
      var memberAfterUpdate = updated.getBody();
      assertThat(memberAfterUpdate).isNotNull();
      assertThat(memberAfterUpdate.id()).isEqualTo(updateInput.id());
      assertThat(memberAfterUpdate.nickname()).isEqualTo(updateInput.nickname());
      assertThat(memberAfterUpdate.emailAddress()).isEqualTo(updateInput.emailAddress());
      assertThat(memberAfterUpdate.version()).isEqualTo(updateInput.version() + 1);
    }

    @Test
    void updateIsRejectedWhenOptimisticLockFails() {
      // given
      var existingMember =
          insert(NewMember.builder().nickname("Alan").emailAddress("alan@kay.com").build());
      var updateInput =
          MemberDto.builder()
              .nickname("John")
              .emailAddress("john@mccarthy.com")
              .id(existingMember.id())
              .version(existingMember.version() + 1)
              .build();

      // when
      var updated =
          testRestTemplate.exchange(
              RequestEntity.put(MEMBER_ENDPOINT + "/{memberId}", existingMember.id())
                  .body(updateInput),
              MemberDto.class);

      // then

      // then
      assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void updateIsRejectedWhenMemberDoesNotExist() {
      // given
      var updateInput =
          MemberDto.builder().nickname("John").emailAddress("john@mccarthy.com").id(2137).build();

      // when
      var updated =
          testRestTemplate.exchange(
              RequestEntity.put(MEMBER_ENDPOINT + "/2137").body(updateInput), String.class);

      // then
      assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void updateIsRejectedWhenIdFromUriDiffersFromBody() {
      // given
      var updateInput =
          MemberDto.builder().nickname("John").emailAddress("john@mccarthy.com").id(2137).build();

      // when
      var updated =
          testRestTemplate.exchange(
              RequestEntity.put(MEMBER_ENDPOINT + "/7312").body(updateInput), String.class);

      // then
      assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @ParameterizedTest
    @MethodSource("incompleteMemberPayloads")
    void failsToUpdateMemberWhenPayloadIsIncomplete(
        MemberDto incompleteMember, List<String> missingFieldNames) {
      // when
      var updated =
          testRestTemplate.exchange(
              RequestEntity.put(MEMBER_ENDPOINT + "/" + incompleteMember.id())
                  .body(incompleteMember),
              String.class);

      // then
      assertThat(updated.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
      var responseBody = updated.getBody();
      assertThat(responseBody).contains(missingFieldNames);
    }
  }
}
