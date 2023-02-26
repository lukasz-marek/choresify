package org.choresify.domain.household.usecase;

import org.choresify.domain.household.model.Household;

public interface UpdateHouseholdUseCase {
  Household execute(Household household);
}
