package org.choresify.application.household.adapter.driven.postgres;

import org.choresify.application.household.adapter.driven.postgres.entity.HouseholdEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface HouseholdsRepository extends CrudRepository<HouseholdEntity, Long> {}
