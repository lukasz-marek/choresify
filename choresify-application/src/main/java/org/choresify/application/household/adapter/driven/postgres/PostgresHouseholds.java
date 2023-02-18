package org.choresify.application.household.adapter.driven.postgres;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.household.adapter.driven.postgres.entity.HouseholdEntity;
import org.choresify.application.member.adapter.driven.postgres.MembersRepository;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
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
  private final HouseholdsRepository householdsRepository;
  private final HouseholdEntityMapper mapper;
  private final MembersRepository membersRepository;
  /*
  For consistency, this requires REPEATABLE_READS isolation level (or lock on members)
   */
  @Override
  public Household insert(@NonNull NewHousehold newHousehold) {
    var entityForInsert = prepareEntity(newHousehold);
    var inserted = householdsRepository.save(entityForInsert);
    return mapper.map(inserted);
  }

  private HouseholdEntity prepareEntity(NewHousehold newHousehold) {
    var entityForInsert = mapper.map(newHousehold);
    var actualMembers = fetchReferencedMembers(entityForInsert);
    if (actualMembers.size() < entityForInsert.getMembers().size()) {
      log.warn(
          "Some members referenced in [{}] are missing - insert will not be performed",
          newHousehold);
      throw new RuntimeException(
          "Some members from [%s] are missing from the database".formatted(newHousehold.members()));
    }

    entityForInsert.setMembers(actualMembers);
    log.info("[{}] is ready to be inserted - proceeding", newHousehold);
    return entityForInsert;
  }

  private Set<MemberEntity> fetchReferencedMembers(HouseholdEntity entityForInsert) {
    return StreamSupport.stream(
            membersRepository
                .findAllById(
                    entityForInsert.getMembers().stream().map(MemberEntity::getId).toList())
                .spliterator(),
            false)
        .collect(Collectors.toSet());
  }
}
