package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.choresify.domain.common.validation.Guards;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
public final class DefaultCreateMemberUseCase implements CreateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  private static void validateNewMember(NewMember newMember) {
    Guards.validateNonNull(newMember, "new member");
    Guards.validateNonNull(newMember.getNickname(), "nickname");
    Guards.validateNonNull(newMember.getEmailAddress(), "email address");
  }

  @Override
  public Member execute(NewMember newMember) {
    validateNewMember(newMember);
    return members.insert(newMember);
  }
}
