package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.Invariants;
import org.choresify.domain.exception.NoSuchEntityException;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultUpdateMemberUseCase implements UpdateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  @Override
  public Member execute(Member member) {
    // todo check if email in use
    Invariants.requireNonNull(member, "member");
    if (!memberExists(member.id())) {
      log.info("Rejecting update of [{}] - no such member exists", member);
      throw new NoSuchEntityException("Cannot update non-existent member");
    }
    log.info("Member exists, proceeding with update to [{}]", member);
    return members.save(member);
  }

  private boolean memberExists(long memberId) {
    log.info("Checking if member with id=[{}] exists", memberId);
    return members.findById(memberId).isPresent();
  }
}
