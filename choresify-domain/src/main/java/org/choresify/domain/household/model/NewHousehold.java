package org.choresify.domain.household.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import lombok.Builder;
import org.choresify.domain.common.sanitation.NameSanitizer;
import org.choresify.domain.exception.Invariants;

@Builder
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Copy.of called in constructor")
public record NewHousehold(String name, Set<HouseholdMember> members) {
  public NewHousehold {
    Invariants.requireNonNull(name, "name");
    Invariants.requireNonNull(members, "members");
    Invariants.requireTrue(!members.isEmpty(), "members must not be empty");
    members.forEach(member -> Invariants.requireNonNull(member, "member"));

    members = Set.copyOf(members);
    name = NameSanitizer.sanitize(name);
  }
}
