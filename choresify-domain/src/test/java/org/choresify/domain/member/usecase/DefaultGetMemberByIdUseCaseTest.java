package org.choresify.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.member.port.InMemoryMembers;
import org.junit.jupiter.api.Test;

class DefaultGetMemberByIdUseCaseTest {
  private final Members members = new InMemoryMembers();
  private final DefaultGetMemberByIdUseCase tested = new DefaultGetMemberByIdUseCase(members);

  @Test
  void returnsMemberWhenItExists() {
    // given
    var existingMember =
        members.insert(
            NewMember.builder().emailAddress("email@example.com").nickname("a nickname").build());

    // when
    var result = tested.execute(existingMember.getId());

    // then
    assertThat(result).contains(existingMember);
  }

  @Test
  void returnsEmptyWhenMemberDoesNotExist() {

    // when
    var result = tested.execute(2137);

    // then
    assertThat(result).isEmpty();
  }
}
