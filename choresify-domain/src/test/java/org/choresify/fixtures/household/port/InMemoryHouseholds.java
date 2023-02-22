package org.choresify.fixtures.household.port;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;

public final class InMemoryHouseholds implements Households {
  private final Map<Long, Household> storage = new LinkedHashMap<>();
  private long nextId = 0;

  @Override
  public Household insert(NewHousehold newHousehold) {
    var household =
        Household.builder()
            .id(nextId++)
            .name(newHousehold.name())
            .members(newHousehold.members())
            .version(0)
            .build();
    storage.put(household.id(), household);
    return household;
  }

  @Override
  public Optional<Household> getById(long householdId) {
    return Optional.ofNullable(storage.get(householdId));
  }
}
