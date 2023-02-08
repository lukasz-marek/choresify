package org.choresify.domain.member.usecase;

import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;
import org.choresify.fixtures.member.port.InMemoryMembers;

class DefaultUpdateMemberUseCaseTest {
  private final Members members = new InMemoryMembers();
  private final Validator<Member> noopValidator = (member) -> {};

  private final DefaultUpdateMemberUseCase tested =
      new DefaultUpdateMemberUseCase(members, noopValidator);
}
