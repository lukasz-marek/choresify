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
              .version(37)
              .build();

      // when
      var memberDto = tested.map(member);

      // then
      assertThat(memberDto.getId()).isEqualTo(21);
      assertThat(memberDto.getEmailAddress()).isEqualTo("john@snow.com");
      assertThat(memberDto.getNickname()).isEqualTo("John Snow");
      assertThat(memberDto.getVersion()).isEqualTo(37);
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
      assertThat(memberDto.getVersion()).isEqualTo(0);
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
      assertThat(newMember.getEmailAddress()).isEqualTo("wanda@maximoff.com");
      assertThat(newMember.getNickname()).isEqualTo("Wanda Maximoff");
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var newMemberDto = NewMemberDto.builder().emailAddress(null).nickname(null).build();

      // when
      var newMember = tested.map(newMemberDto);

      // then
      assertThat(newMember.getEmailAddress()).isNull();
      assertThat(newMember.getNickname()).isNull();
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
              .version(37)
              .build();

      // when
      var member = tested.map(memberDto);

      // then
      assertThat(member.getId()).isEqualTo(21);
      assertThat(member.getEmailAddress()).isEqualTo("john@snow.com");
      assertThat(member.getNickname()).isEqualTo("John Snow");
      assertThat(member.getVersion()).isEqualTo(37);
    }

    @Test
    void nullsAreMappedToNulls() {
      // given
      var memberDto = MemberDto.builder().build();

      // when
      var member = tested.map(memberDto);

      // then
      assertThat(member.getId()).isEqualTo(0);
      assertThat(member.getEmailAddress()).isNull();
      assertThat(member.getNickname()).isNull();
      assertThat(member.getVersion()).isEqualTo(0);
    }
  }
}
