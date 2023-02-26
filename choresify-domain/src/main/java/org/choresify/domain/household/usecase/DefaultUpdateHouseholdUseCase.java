package org.choresify.domain.household.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.ConflictingDataException;
import org.choresify.domain.exception.Invariants;
import org.choresify.domain.exception.NoSuchEntityException;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.port.Households;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public class DefaultUpdateHouseholdUseCase implements UpdateHouseholdUseCase {

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Households households;

  @Override
  public Household execute(Household household) {
    checkPreconditions(household);
    log.info("Preconditions checks successful - updating [{}]", household);
    return households
        .updateWithOptimisticLock(household)
        .orElseThrow(() -> new ConflictingDataException("Optimistic lock failed"));
  }

  private void checkPreconditions(Household household) {
    log.info("Checking precondition for [{}]", household);
    Invariants.requireNonNull(household, "household");
    Invariants.requireTrue(
        !household.members().isEmpty(), "At least one member must be referenced");
    Invariants.requireTrue(
        allReferencedMembersExist(household), "Some of referenced members do not exist");
    checkThatHouseholdExist(household);
  }

  private void checkThatHouseholdExist(Household household) {
    if (households.getById(household.id()).isEmpty()) {
      throw new NoSuchEntityException("Cannot update non-existent household");
    }
  }

  private boolean allReferencedMembersExist(Household newHousehold) {
    var existingReferencedMembers = getReferencedMembers(newHousehold);
    return existingReferencedMembers.size() == newHousehold.members().size();
  }

  private Map<Long, Member> getReferencedMembers(Household newHousehold) {
    var referencedIds = newHousehold.members().stream().map(HouseholdMember::memberId).toList();
    return members.findById(referencedIds);
  }
}
