package org.choresify.domain.household.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.Invariants;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.port.Members;

@Slf4j
@RequiredArgsConstructor
public class DefaultCreateHouseholdUseCase implements CreateHouseholdUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Households households;

  @Override
  public Household execute(NewHousehold newHousehold) {
    checkInvariants(newHousehold);
    log.info("All validations passed - [{}] will be inserted", newHousehold);
    return households.insert(newHousehold);
  }

  private void checkInvariants(NewHousehold newHousehold) {
    Invariants.requireNonNull(newHousehold, "new household");
    Invariants.requireTrue(
        allReferencedMembersExist(newHousehold), "Some of referenced members do not exist");
  }

  private boolean allReferencedMembersExist(NewHousehold newHousehold) {
    var existingReferencedMembers = getReferencedMembers(newHousehold);
    return existingReferencedMembers.size() == newHousehold.members().size();
  }

  private Map<Long, Member> getReferencedMembers(NewHousehold newHousehold) {
    var referencedIds = newHousehold.members().stream().map(HouseholdMember::getMemberId).toList();
    return members.findById(referencedIds);
  }
}
