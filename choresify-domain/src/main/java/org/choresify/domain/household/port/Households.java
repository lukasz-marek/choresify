package org.choresify.domain.household.port;

import java.util.Optional;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;

public interface Households {

  /**
   * Inserts a household into the storage.
   *
   * @param newHousehold
   * @return empty if any of referenced members does not exist, inserted value otherwise
   */
  Optional<Household> insert(NewHousehold newHousehold);
}
