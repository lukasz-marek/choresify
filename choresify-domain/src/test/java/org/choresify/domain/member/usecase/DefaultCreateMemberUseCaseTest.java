package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.vavr.control.Validation;
import java.util.Optional;
import org.choresify.domain.error.Category;
import org.choresify.domain.error.Failure;
import org.choresify.domain.error.FailureDetails;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class DefaultCreateMemberUseCaseTest {

  private final Members members = Mockito.mock(Members.class);
  private final NewMemberValidator newMemberValidator = Mockito.mock(NewMemberValidator.class);
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
    when(newMemberValidator.validate(newMember)).thenReturn(Validation.valid(newMember));
    when(members.findByEmail(newMember.getEmailAddress())).thenReturn(Optional.empty());
    when(members.insert(newMember)).thenReturn(expected);

    // when
    var result = tested.execute(newMember);

    // then
    assertThat(result.get()).isEqualTo(expected);
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
    when(newMemberValidator.validate(newMember)).thenReturn(Validation.valid(newMember));
    when(members.findByEmail(newMember.getEmailAddress())).thenReturn(Optional.of(existing));

    // when
    var result = tested.execute(newMember);

    // then
    assertThat(result.isLeft()).isTrue();
    assertThat(result.getLeft().getFailureDetails())
        .containsExactly(FailureDetails.of(Category.PRECONDITION, "Email address already in use"));
  }

  @Test
  void rejectsMemberWhenValidatorRejectsIt() {
    // given
    var newMember =
        NewMember.builder().nickname("Adam Smith").emailAddress("adam@smith.com").build();
    when(newMemberValidator.validate(newMember))
        .thenReturn(Validation.invalid(Failure.of(Category.VALIDATION, "Something went wrong")));

    // when
    var result = tested.execute(newMember);

    // then
    assertThat(result.isLeft()).isTrue();
    assertThat(result.getLeft().getFailureDetails()).hasSize(1);
    assertThat(result.getLeft().getFailureDetails())
        .containsExactly(FailureDetails.of(Category.VALIDATION, "Something went wrong"));
  }
}
