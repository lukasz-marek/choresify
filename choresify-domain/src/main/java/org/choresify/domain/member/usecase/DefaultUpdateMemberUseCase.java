package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
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

  private static void handleMissingMember(Member member) {
    log.info("Rejecting update of [{}] - no such member exists", member);
    throw new NoSuchEntityException("Cannot update non-existent member");
  }

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
    var maybeExistingMember = members.findById(member.id());
    maybeExistingMember.ifPresentOrElse(
        existingMember -> checkEmailAddress(member, existingMember),
        () -> handleMissingMember(member));
  }

  private void checkEmailAddress(Member member, Member existingMember) {
    if (!Objects.equals(existingMember.emailAddress(), member.emailAddress())
        && emailAlreadyInUse(member)) {
      log.info(
          "Rejecting update of [{}] - email address already in use by a different member", member);
      throw new ConflictingDataException("Email address already in use by a different member");
    }
  }

  private boolean emailAlreadyInUse(Member member) {
    return members.findByEmail(member.emailAddress()).isPresent();
  }
}
