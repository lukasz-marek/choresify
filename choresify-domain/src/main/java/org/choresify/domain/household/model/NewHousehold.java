package org.choresify.domain.household.model;

import java.util.Set;
import lombok.Builder;
import org.choresify.domain.common.sanitation.NameSanitizer;
import org.choresify.domain.exception.Invariants;

@Builder
public record NewHousehold(String name, Set<HouseholdMember> members) {
  public NewHousehold {
    Invariants.requireNonNull(name, "name");
    Invariants.requireNonNull(members, "members");
    members.forEach(member -> Invariants.requireNonNull(member, "member"));

    members = Set.copyOf(members);
    name = NameSanitizer.sanitize(name);
  }
}
