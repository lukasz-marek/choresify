package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.exception.DomainException;
import org.choresify.domain.exception.DomainException.PreconditionFailedException;
import org.choresify.domain.exception.DomainException.ValidationException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultCreateMemberUseCaseTest {

  private final Members members = Mockito.mock(Members.class);
  private final Validator<NewMember> newMemberValidator = (newMember) -> {};
  private final DefaultCreateMemberUseCase tested =
      new DefaultCreateMemberUseCase(members, newMemberValidator);

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
    when(members.findByEmail(newMember.getEmailAddress())).thenReturn(Optional.empty());
    when(members.insert(newMember)).thenReturn(expected);

    // when
    var result = tested.execute(newMember);

    // then
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void failsToCreateNewMemberWhenEmailAlreadyInUse() {
    // given
    var newMember =
        NewMember.builder().nickname("Adam Smith").emailAddress("adam@smith.com").build();
    var existing =
        Member.builder()
            .nickname("John Smith")
            .emailAddress("adam@smith.com")
            .id(21)
            .version(37)
            .build();
    when(members.findByEmail(newMember.getEmailAddress())).thenReturn(Optional.of(existing));

    // when
    var result =
        catchThrowableOfType(() -> tested.execute(newMember), PreconditionFailedException.class);

    // then
    assertThat(result.getMessage()).isEqualTo("Email address already in use");
  }

  @Test
  void rejectsMemberWhenValidatorRejectsIt() {
    // given
    var tested =
        new DefaultCreateMemberUseCase(
            members,
            (member) -> {
              throw new ValidationException("Something went wrong");
            });
    var newMember =
        NewMember.builder().nickname("Adam Smith").emailAddress("adam@smith.com").build();

    // when
    var result =
        catchThrowableOfType(
            () -> tested.execute(newMember), DomainException.ValidationException.class);

    // then
    assertThat(result.getMessage()).isEqualTo("Something went wrong");
  }
}
