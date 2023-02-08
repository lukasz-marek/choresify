package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultUpdateMemberUseCase implements UpdateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  private final Validator<Member> memberValidator;

  @Override
  public Member execute(Member newValue) {
    return null;
  }
}
