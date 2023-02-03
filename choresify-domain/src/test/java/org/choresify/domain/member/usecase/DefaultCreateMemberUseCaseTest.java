package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.choresify.domain.common.validation.DomainException.DomainValidationException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultCreateMemberUseCaseTest {

  private final Members members = Mockito.mock(Members.class);
  private final DefaultCreateMemberUseCase tested = new DefaultCreateMemberUseCase(members);

  @Test
  void createsNewMemberWhenValid() {
    // given
    var newMember =
        NewMember.builder().nickname("Adam Smith").emailAddress("adam@smith.com").build();
    var expected =
        Member.builder()
            .nickname("Adam Smith")
            .emailAddress("adam@smith.com")
            .id(21)
            .version(37)
            .build();
    when(members.insert(newMember)).thenReturn(expected);

    // when
    var result = tested.execute(newMember);

    // then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void rejectsMemberWhenItIsNull() {
    // when
    var result =
        Assertions.catchThrowableOfType(
            () -> tested.execute(null), DomainValidationException.class);

    // then
    assertThat(result).hasMessageContaining("new member");
  }

  @Test
  void rejectsMemberWhenNickNameIsNull() {
    // given
    var newMember = NewMember.builder().nickname(null).emailAddress("adam@smith.com").build();

    // when
    var result =
        Assertions.catchThrowableOfType(
            () -> tested.execute(newMember), DomainValidationException.class);

    // then
    assertThat(result).hasMessageContaining("nickname");
  }

  @Test
  void rejectsMemberWhenEmailIsNull() {
    // given
    var newMember = NewMember.builder().nickname("a nickname").emailAddress(null).build();

    // when
    var result =
        Assertions.catchThrowableOfType(
            () -> tested.execute(newMember), DomainValidationException.class);

    // then
    assertThat(result).hasMessageContaining("email address");
  }
}
