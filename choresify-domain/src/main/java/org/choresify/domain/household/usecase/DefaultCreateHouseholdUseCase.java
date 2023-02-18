package org.choresify.domain.household.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.exception.Invariants;
import org.choresify.domain.exception.NoSuchEntityException;
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

  private static void checkInvariants(NewHousehold newHousehold) {
    Invariants.requireNonNull(newHousehold, "new household");
    Invariants.requireTrue(
        !newHousehold.members().isEmpty(), "At least one member must be referenced");
  }

  @Override
  public Household execute(NewHousehold newHousehold) {
    checkInvariants(newHousehold);
    var existingReferencedMembers = getReferencedMembers(newHousehold);
    if (existingReferencedMembers.size() < newHousehold.members().size()) {
      log.info("Some of referenced members do not exist - [{}] will not be created", newHousehold);
      throw new NoSuchEntityException("Some of referenced members do not exist");
    }
    log.info("All validations passed - [{}] will be inserted", newHousehold);
    return households.insert(newHousehold);
  }

  private Map<Long, Member> getReferencedMembers(NewHousehold newHousehold) {
    var referencedIds = newHousehold.members().stream().map(HouseholdMember::memberId).toList();
    return members.findById(referencedIds);
  }
}
