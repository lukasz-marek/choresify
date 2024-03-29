package org.choresify.application.member.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.domain.member.model.Member;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MemberDtoMapperTest {
  private final MemberDtoMapper tested = Mappers.getMapper(MemberDtoMapper.class);

  @Nested
  class MemberMapping {
    @Test
    void mapsMemberToMemberDto() {
      // given
      var member =
          Member.builder()
              .id(21)
              .nickname("John Snow")
              .emailAddress("john@snow.com")
              .version(5)
              .build();

      // when
      var memberDto = tested.map(member);

      // then
      assertThat(memberDto.id()).isEqualTo(21);
      assertThat(member.version()).isEqualTo(5);
      assertThat(memberDto.emailAddress()).isEqualTo("john@snow.com");
      assertThat(memberDto.nickname()).isEqualTo("John Snow");
    }
  }

  @Nested
  class NewMemberMapping {
    @Test
    void mapsNewMemberDtoToNewMember() {
      // given
      var newMemberDto =
          NewMemberDto.builder()
              .emailAddress("wanda@maximoff.com")
              .nickname("Wanda Maximoff")
              .build();

      // when
      var newMember = tested.map(newMemberDto);

      // then
      assertThat(newMember.emailAddress()).isEqualTo("wanda@maximoff.com");
      assertThat(newMember.nickname()).isEqualTo("Wanda Maximoff");
    }
  }

  @Nested
  class MemberDtoMapping {
    @Test
    void mapsMemberDtoToMember() {
      // given
      var memberDto =
          MemberDto.builder()
              .id(21)
              .nickname("John Snow")
              .emailAddress("john@snow.com")
              .version(2L)
              .build();

      // when
      var member = tested.map(memberDto);

      // then
      assertThat(member.id()).isEqualTo(21);
      assertThat(member.version()).isEqualTo(2);
      assertThat(member.emailAddress()).isEqualTo("john@snow.com");
      assertThat(member.nickname()).isEqualTo("John Snow");
    }
  }
}
