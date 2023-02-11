package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;

import org.choresify.domain.exception.ConflictingDataException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultCreateMemberUseCaseTest {

  private final Members members = new InMemoryMembers();
  private final DefaultCreateMemberUseCase tested = new DefaultCreateMemberUseCase(members);

  @Test
  void createsNewMemberWhenValid() {
    // given
    var newMember =
        NewMember.builder().nickname("Adam Smith").emailAddress("adam@smith.com").build();
    var expected =
        Member.builder().nickname("Adam Smith").emailAddress("adam@smith.com").id(1).build();

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
    members.insert(newMember);

    // when
    var result =
        catchThrowableOfType(() -> tested.execute(newMember), ConflictingDataException.class);

    // then
    assertThat(result.getMessage()).isEqualTo("Email address already in use");
  }
}
