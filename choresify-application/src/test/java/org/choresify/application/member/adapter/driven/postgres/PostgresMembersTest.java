package org.choresify.application.member.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
class PostgresMembersTest {
  private final PostgresMembers tested;

  @Autowired
  PostgresMembersTest(PostgresMembers postgresMembers) {
    this.tested = postgresMembers;
  }

  @Nested
  class Insertion {
    @Test
    void insertionIsSuccessful() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.insert(newMember);

      // then
      assertThat(afterSave.getId()).isPositive();
      assertThat(afterSave.getNickname()).isEqualTo("a nickname");
      assertThat(afterSave.getEmailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void insertionIsPersistent() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.insert(newMember);
      var read = tested.findById(afterSave.getId());

      // then
      assertThat(read).contains(afterSave);
    }
  }

  @Nested
  class FindByEmail {
    @Test
    void returnsEmptyWhenNoEmailMatches() {
      // when
      var result = tested.findByEmail("email@example.com");

      // then
      assertThat(result).isEmpty();
    }

    @Test
    void returnsMemberByEmail() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();
      var existingMember = tested.insert(newMember);

      // when
      var result = tested.findByEmail("email@example.com");

      // then
      assertThat(result).isNotEmpty();
      assertThat(result).contains(existingMember);
    }
  }

  @Nested
  class FindById {
    @Test
    void returnsMemberWhenItExists() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();
      var existingMember = tested.insert(newMember);

      // when
      var result = tested.findById(existingMember.getId());

      // then
      assertThat(result).isNotEmpty();
      assertThat(result).contains(existingMember);
    }

    @Test
    void returnsEmptyWhenMemberDoesNotExist() {
      // when
      var result = tested.findById(2137);

      // then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  class Save {
    @Test
    void createsNewMemberWhenItDoesNotExist() {
      // given
      var newMember =
          Member.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.save(newMember);

      // then
      assertThat(afterSave.getId()).isPositive();
      assertThat(afterSave.getNickname()).isEqualTo("a nickname");
      assertThat(afterSave.getEmailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void creationIsPersistent() {
      // given
      var newMember =
          Member.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.save(newMember);
      var read = tested.findById(afterSave.getId());

      // then
      assertThat(read).contains(afterSave);
    }

    @Test
    void overridesMemberWhenItExists() {
      // given
      var existing =
          tested.insert(
              NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());
      var override =
          Member.builder()
              .id(existing.getId())
              .emailAddress("different@example.com")
              .nickname("a different nickname")
              .build();

      // when
      var afterSave = tested.save(override);

      // then
      assertThat(afterSave.getId()).isEqualTo(existing.getId());
      assertThat(afterSave.getNickname()).isEqualTo("a different nickname");
      assertThat(afterSave.getEmailAddress()).isEqualTo("different@example.com");
    }

    @Test
    void updateIsPersistent() {
      // given
      var existing =
          tested.insert(
              NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());
      var override =
          Member.builder()
              .id(existing.getId())
              .emailAddress("different@example.com")
              .nickname("a different nickname")
              .build();

      // when
      var afterSave = tested.save(override);
      var read = tested.findById(afterSave.getId());

      // then
      assertThat(read).contains(afterSave);
    }
  }
}
