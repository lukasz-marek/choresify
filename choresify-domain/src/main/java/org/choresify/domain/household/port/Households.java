package org.choresify.domain.household.port;

import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;

public interface Households {

  Household insert(NewHousehold newHousehold);
}
