package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.ConflictingDataException;
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
    Invariants.requireNonNull(member, "member");
    checkPreconditions(member);
    log.info("Checks complete, proceeding with update to [{}]", member);
    return members
        .updateWithOptimisticLock(member)
        .orElseThrow(() -> new ConflictingDataException("Optimistic lock failed"));
  }

  private void checkPreconditions(Member member) {
    // todo try to fetch member just once if ids match
    if (!memberExists(member.id())) {
      log.info("Rejecting update of [{}] - no such member exists", member);
      throw new NoSuchEntityException("Cannot update non-existent member");
    }
    if (emailInUseByDifferentMember(member)) {
      log.info(
          "Rejecting update of [{}] - email address already in use by a different member", member);
      throw new ConflictingDataException("Email address already in use by a different member");
    }
  }

  private boolean emailInUseByDifferentMember(Member member) {
    return members
        .findByEmail(member.emailAddress())
        .map(existingMember -> existingMember.id() != member.id())
        .orElse(false);
  }

  private boolean memberExists(long memberId) {
    log.info("Checking if member with id=[{}] exists", memberId);
    return members.findById(memberId).isPresent();
  }
}
