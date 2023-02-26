package org.choresify.domain.household.model;

import lombok.Value;

@Value(staticConstructor = "of")
public class HouseholdMember {
  long memberId;
}
