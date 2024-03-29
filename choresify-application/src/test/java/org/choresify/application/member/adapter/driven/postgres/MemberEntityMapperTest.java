package org.choresify.application.member.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.member.model.Member;
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
      assertThat(entity.getVersion()).isEqualTo(0);
      assertThat(entity.getId()).isNull();
      assertThat(entity.getNickname()).isEqualTo("a nickname");
      assertThat(entity.getEmailAddress()).isEqualTo("email@example.com");
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
              .id(37L)
              .version(5L)
              .build();

      // when
      var member = tested.map(entity);

      // then
      assertThat(member).isNotNull();
      assertThat(member.version()).isEqualTo(5);
      assertThat(member.id()).isEqualTo(37);
      assertThat(member.nickname()).isEqualTo("a nickname");
      assertThat(member.emailAddress()).isEqualTo("email@example.com");
    }
  }

  @Nested
  class MemberToEntity {
    @Test
    void allFieldsAreMapped() {
      // given
      var member =
          Member.builder()
              .emailAddress("email@example.com")
              .nickname("a nickname")
              .id(37L)
              .version(21)
              .build();

      // when
      var entity = tested.map(member);

      // then
      assertThat(entity).isNotNull();
      assertThat(entity.getVersion()).isEqualTo(21);
      assertThat(entity.getId()).isEqualTo(37);
      assertThat(entity.getNickname()).isEqualTo("a nickname");
      assertThat(entity.getEmailAddress()).isEqualTo("email@example.com");
    }
  }
}
