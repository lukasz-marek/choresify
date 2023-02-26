package org.choresify.application.member.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collections;
import java.util.List;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.fixtures.PersistenceTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@PersistenceTest
class PostgresMembersTest {
  @SuppressFBWarnings @Autowired private PostgresMembers tested;

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
      assertThat(afterSave.id()).isPositive();
      assertThat(afterSave.nickname()).isEqualTo("a nickname");
      assertThat(afterSave.emailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void insertionIsPersistent() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.insert(newMember);
      var read = tested.findById(afterSave.id());

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
      var result = tested.findById(existingMember.id());

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
      assertThat(afterSave.id()).isPositive();
      assertThat(afterSave.nickname()).isEqualTo("a nickname");
      assertThat(afterSave.emailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void creationIsPersistent() {
      // given
      var newMember =
          Member.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.save(newMember);
      var read = tested.findById(afterSave.id());

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
              .id(existing.id())
              .emailAddress("different@example.com")
              .nickname("a different nickname")
              .build();

      // when
      var afterSave = tested.save(override);

      // then
      assertThat(afterSave.id()).isEqualTo(existing.id());
      assertThat(afterSave.nickname()).isEqualTo("a different nickname");
      assertThat(afterSave.emailAddress()).isEqualTo("different@example.com");
    }

    @Test
    void updateIsPersistent() {
      // given
      var existing =
          tested.insert(
              NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());
      var override =
          Member.builder()
              .id(existing.id())
              .emailAddress("different@example.com")
              .nickname("a different nickname")
              .build();

      // when
      var afterSave = tested.save(override);
      var read = tested.findById(afterSave.id());

      // then
      assertThat(read).contains(afterSave);
    }
  }

  @Nested
  class UpdateWithOptimisticLock {

    @Test
    void overwritesExistingMemberWhenVersionsMatch() {
      // given
      var existingMember =
          tested.insert(
              NewMember.builder().nickname("a nickname").emailAddress("email@example.com").build());
      var forUpdate =
          Member.builder()
              .emailAddress(existingMember.emailAddress())
              .nickname("new nickname")
              .id(existingMember.id())
              .version(existingMember.version())
              .build();

      // when
      var result = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(result)
          .contains(
              Member.builder()
                  .id(forUpdate.id())
                  .emailAddress(forUpdate.emailAddress())
                  .nickname(forUpdate.nickname())
                  .version(forUpdate.version() + 1)
                  .build());
    }

    @Test
    void updateIsPersistent() {
      // given
      var existingMember =
          tested.insert(
              NewMember.builder().nickname("a nickname").emailAddress("email@example.com").build());
      var forUpdate = existingMember.toBuilder().emailAddress("different@example.com").build();

      // when
      tested.updateWithOptimisticLock(forUpdate);
      var result = tested.findById(forUpdate.id());

      // then
      assertThat(result).contains(forUpdate.toBuilder().version(forUpdate.version() + 1).build());
    }

    @Test
    void failsToUpdateWhenMemberDoesNotExist() {
      // given
      var forUpdate =
          Member.builder()
              .emailAddress("email@example.com")
              .nickname("new nickname")
              .id(2137L)
              .version(12345)
              .build();

      // when
      var result = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(longs = {1, -1, 2, -2})
    void failsToUpdateWhenVersionsDoNotMatch(long versionDiff) {
      // given
      var existingMember =
          tested.insert(
              NewMember.builder().nickname("a nickname").emailAddress("email@example.com").build());
      var forUpdate =
          Member.builder()
              .emailAddress("email@example.com")
              .nickname("new nickname")
              .id(existingMember.id())
              .version(existingMember.version() - versionDiff)
              .build();

      // when
      var result = tested.updateWithOptimisticLock(forUpdate);

      // then
      assertThat(result).isEmpty();
    }
  }

  @Nested
  class GetManyByIds {
    @Test
    void returnsExistingMembersByIds() {
      // given
      var existingMember1 =
          tested.insert(
              NewMember.builder()
                  .nickname("a nickname")
                  .emailAddress("email1@example.com")
                  .build());
      var existingMember2 =
          tested.insert(
              NewMember.builder()
                  .nickname("a nickname")
                  .emailAddress("email2@example.com")
                  .build());
      // when
      var results = tested.findById(List.of(existingMember1.id(), existingMember2.id()));

      // then
      assertThat(results).hasSize(2);
      assertThat(results.get(existingMember1.id())).isEqualTo(existingMember1);
      assertThat(results.get(existingMember2.id())).isEqualTo(existingMember2);
    }

    @Test
    void nonExistentMembersAreIgnored() {
      // given
      var existingMember1 =
          tested.insert(
              NewMember.builder()
                  .nickname("a nickname")
                  .emailAddress("email1@example.com")
                  .build());
      var existingMember2 =
          tested.insert(
              NewMember.builder()
                  .nickname("a nickname")
                  .emailAddress("email2@example.com")
                  .build());
      // when
      var results =
          tested.findById(List.of(existingMember1.id(), 2137L, existingMember2.id(), 1410L));

      // then
      assertThat(results).hasSize(2);
      assertThat(results.get(existingMember1.id())).isEqualTo(existingMember1);
      assertThat(results.get(existingMember2.id())).isEqualTo(existingMember2);
    }

    @Test
    void returnsEmptyMapWhenNothingMatches() {
      // when
      var results = tested.findById(List.of(2137L, 1410L));

      // then
      assertThat(results).isEmpty();
    }

    @Test
    void returnsEmptyMapOnEmptyInput() {
      // when
      var results = tested.findById(Collections.emptyList());

      // then
      assertThat(results).isEmpty();
    }
  }
}
