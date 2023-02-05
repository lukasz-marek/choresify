package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.exception.DomainException.FailedPreconditionException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultCreateMemberUseCase implements CreateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  private final Validator<NewMember> newMemberValidator;

  @Override
  public Member execute(NewMember newMember) {
    newMemberValidator.validate(newMember);
    if (isEmailInUse(newMember.getEmailAddress())) {
      log.info(
          "Member email [{}] already exists, insertion of [{}] canceled",
          newMember.getEmailAddress(),
          newMember);
      throw new FailedPreconditionException("Email address already in use");
    }
    log.info("Saving [{}]", newMember);
    return members.insert(newMember);
  }

  private boolean isEmailInUse(String email) {
    var existingMemberWithSameEmail = members.findByEmail(email);
    return existingMemberWithSameEmail.isPresent();
  }
}
