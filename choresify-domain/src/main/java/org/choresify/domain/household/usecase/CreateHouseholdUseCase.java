package org.choresify.domain.household.usecase;

import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;

public interface CreateHouseholdUseCase {
  Household execute(NewHousehold newHousehold);
}
