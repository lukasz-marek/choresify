package org.choresify.domain.household.usecase;

import java.util.Optional;
import org.choresify.domain.household.model.Household;

public interface GetHouseholdByIdUseCase {

  Optional<Household> execute(long id);
}
