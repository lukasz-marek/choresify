package org.choresify.application.household.adapter.driving.rest;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Set;
import lombok.Builder;

@Builder
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Copy.of called in constructor")
record HouseholdDto(long id, String name, Set<HouseholdMemberDto> members, long version) {
  HouseholdDto {
    members = Set.copyOf(members);
  }
}
