package org.choresify.application.member.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.domain.member.model.NewMember;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MemberEntityMapperTest {
  private final MemberEntityMapper tested = Mappers.getMapper(MemberEntityMapper.class);

  @Nested
  class NewMemberToEntity {
    @Test
    void allFieldsAreMapped() {
      // given
      var newMember =
          NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build();

      // when
      var entity = tested.map(newMember);

      // then
      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isNull();
      assertThat(entity.getVersion()).isNull();
      assertThat(entity.getNickname()).isEqualTo("a nickname");
      assertThat(entity.getEmailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var newMember = NewMember.builder().emailAddress(null).nickname(null).build();

      // when
      var entity = tested.map(newMember);

      // then
      assertThat(entity).isNotNull();
      assertThat(entity.getId()).isNull();
      assertThat(entity.getVersion()).isNull();
      assertThat(entity.getNickname()).isNull();
      assertThat(entity.getEmailAddress()).isNull();
    }
  }

  @Nested
  class EntityToMember {
    @Test
    void allFieldsAreMapped() {
      // given
      var entity =
          MemberEntity.builder()
              .emailAddress("email@example.com")
              .nickname("a nickname")
              .version(21L)
              .id(37L)
              .build();

      // when
      var member = tested.map(entity);

      // then
      assertThat(member).isNotNull();
      assertThat(member.getId()).isEqualTo(37);
      assertThat(member.getVersion()).isEqualTo(21);
      assertThat(member.getNickname()).isEqualTo("a nickname");
      assertThat(member.getEmailAddress()).isEqualTo("email@example.com");
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var entity =
          MemberEntity.builder().emailAddress(null).nickname(null).version(null).id(null).build();

      // when
      var member = tested.map(entity);

      // then
      assertThat(member).isNotNull();
      assertThat(member.getId()).isEqualTo(0);
      assertThat(member.getVersion()).isEqualTo(0);
      assertThat(member.getNickname()).isEqualTo(null);
      assertThat(member.getEmailAddress()).isEqualTo(null);
    }
  }
}
