package org.choresify.domain.member.usecase;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
public final class DefaultGetMemberByIdUseCase implements GetMemberByIdUseCase {
  private final Members members;

  @Override
  public Optional<Member> execute(long memberId) {
    return members.get(memberId);
  }
}
