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
          Member.builder().id(21).nickname("John Snow").emailAddress("john@snow.com").build();

      // when
      var memberDto = tested.map(member);

      // then
      assertThat(memberDto.getId()).isEqualTo(21);
      assertThat(memberDto.getEmailAddress()).isEqualTo("john@snow.com");
      assertThat(memberDto.getNickname()).isEqualTo("John Snow");
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var member = Member.builder().build();

      // when
      var memberDto = tested.map(member);

      // then
      assertThat(memberDto.getId()).isEqualTo(0);
      assertThat(memberDto.getEmailAddress()).isNull();
      assertThat(memberDto.getNickname()).isNull();
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

    @Test
    void nullsAreMappedToNulls() {
      // given
      var newMemberDto = NewMemberDto.builder().emailAddress(null).nickname(null).build();

      // when
      var newMember = tested.map(newMemberDto);

      // then
      assertThat(newMember.emailAddress()).isNull();
      assertThat(newMember.nickname()).isNull();
    }
  }

  @Nested
  class MemberDtoMapping {
    @Test
    void mapsMemberDtoToMember() {
      // given
      var memberDto =
          MemberDto.builder().id(21).nickname("John Snow").emailAddress("john@snow.com").build();

      // when
      var member = tested.map(memberDto);

      // then
      assertThat(member.id()).isEqualTo(21);
      assertThat(member.emailAddress()).isEqualTo("john@snow.com");
      assertThat(member.nickname()).isEqualTo("John Snow");
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var memberDto = MemberDto.builder().build();

      // when
      var member = tested.map(memberDto);

      // then
      assertThat(member.id()).isEqualTo(0);
      assertThat(member.emailAddress()).isNull();
      assertThat(member.nickname()).isNull();
    }
  }
}
