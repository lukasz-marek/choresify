package org.choresify.application.household.adapter.driven.postgres;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.MANDATORY)
@Slf4j
@RequiredArgsConstructor
class PostgresHouseholds implements Households {
  private final HouseholdRepository householdRepository;
  private final HouseholdEntityMapper mapper;

  @Override
  public Household insert(NewHousehold newHousehold) {
    return null;
  }
}
