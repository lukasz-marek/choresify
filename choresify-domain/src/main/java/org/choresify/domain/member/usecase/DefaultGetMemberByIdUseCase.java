package org.choresify.domain.member.usecase;

import java.util.Optional;
import org.choresify.domain.member.model.Member;

public final class DefaultGetMemberByIdUseCase implements GetMemberByIdUseCase {

  @Override
  public Optional<Member> execute(long memberId) {
    return Optional.empty();
  }
}
