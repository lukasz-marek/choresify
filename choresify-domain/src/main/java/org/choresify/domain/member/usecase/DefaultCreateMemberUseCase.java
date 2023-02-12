package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.ConflictingDataException;
import org.choresify.domain.exception.Invariants;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultCreateMemberUseCase implements CreateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  @Override
  public Member execute(NewMember newMember) {
    Invariants.requireNonNull(newMember, "newMember");
    if (isEmailInUse(newMember.emailAddress())) {
      log.info(
          "Member email [{}] already exists, insertion of [{}] canceled",
          newMember.emailAddress(),
          newMember);
      throw new ConflictingDataException("Email address already in use");
    }
    log.info("Saving [{}]", newMember);
    return members.insert(newMember);
  }

  private boolean isEmailInUse(String email) {
    var existingMemberWithSameEmail = members.findByEmail(email);
    return existingMemberWithSameEmail.isPresent();
  }
}
