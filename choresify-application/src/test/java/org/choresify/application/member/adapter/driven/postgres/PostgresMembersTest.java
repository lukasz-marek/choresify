package org.choresify.application.member.adapter.driven.postgres;

import org.assertj.core.api.Assertions;
import org.choresify.domain.member.model.NewMember;
import org.choresify.fixtures.IntegrationTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
class PostgresMembersTest {

  @Autowired private PostgresMembers tested;

  @Nested
  @Transactional
  class Insertion {
    @Test
    void insertionIsSuccessful() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var afterSave = tested.insert(newMember);

      // then
      Assertions.assertThat(afterSave.isValid()).isTrue();
      var createdMember = afterSave.get();
      Assertions.assertThat(createdMember.getId()).isPositive();
      Assertions.assertThat(createdMember.getVersion()).isEqualTo(0L);
      Assertions.assertThat(createdMember.getNickname()).isEqualTo("a nickname");
      Assertions.assertThat(createdMember.getEmailAddress()).isEqualTo("email@example.com");
    }
  }
}
