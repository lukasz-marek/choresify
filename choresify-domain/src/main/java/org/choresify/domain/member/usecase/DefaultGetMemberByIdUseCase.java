package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
public final class DefaultGetMemberByIdUseCase implements GetMemberByIdUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  @Override
  public Optional<Member> execute(long memberId) {
    return members.findById(memberId);
  }
}
