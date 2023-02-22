package org.choresify.domain.household.port;

import java.util.Optional;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;

public interface Households {

  Household insert(NewHousehold newHousehold);

  Optional<Household> getById(long householdId);

  Optional<Household> updateWithOptimisticLock(Household household);
}
