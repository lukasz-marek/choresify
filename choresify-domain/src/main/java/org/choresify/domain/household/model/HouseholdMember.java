package org.choresify.domain.household.model;

import org.choresify.domain.exception.Invariants;

public record HouseholdMember(long memberId, MemberRole role) {
  public HouseholdMember {
    Invariants.requireNonNull(role, "role");
  }
}
