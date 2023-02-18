package org.choresify.domain.household.model;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import lombok.Builder;
import org.choresify.domain.common.sanitation.NameSanitizer;
import org.choresify.domain.exception.Invariants;

@Builder
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Copy.of called in constructor")
public record Household(long id, String name, Set<HouseholdMember> members, long version) {

  public Household {
    Invariants.requireNonNull(name, "name");
    Invariants.requireNonNull(members, "members");
    members.forEach(member -> Invariants.requireNonNull(member, "members"));

    name = NameSanitizer.sanitize(name);
    members = Set.copyOf(members);
  }
}
