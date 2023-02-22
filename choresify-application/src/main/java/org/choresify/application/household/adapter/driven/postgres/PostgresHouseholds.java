package org.choresify.application.household.adapter.driven.postgres;

import java.util.Objects;
import java.util.Optional;
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
    var entityForInsert = createEntity(newHousehold);
    var inserted = householdsRepository.save(entityForInsert);
    return mapper.map(inserted);
  }

  @Override
  public Optional<Household> getById(long householdId) {
    return householdsRepository.findById(householdId).map(mapper::map);
  }

  /*
  For correctness, requires REPEATABLE_READS or SELECT FOR UPDATE
   */
  @Override
  public Optional<Household> updateWithOptimisticLock(Household household) {
    var existingHousehold = householdsRepository.findById(household.id());
    var versionMatches =
        existingHousehold.map(that -> Objects.equals(that.getId(), household.id())).orElse(false);
    if (versionMatches) {
      log.info("Optimistic locking of [{}] successful - update will be performed", household);
      var entity = createEntity(household);
      entity.setVersion(entity.getVersion() + 1);
      return Optional.of(householdsRepository.save(entity)).map(mapper::map);
    }
    log.info("Optimistic locking failed for [{}] - update ignored", household);
    return Optional.empty();
  }

  private HouseholdEntity createEntity(Household household) {
    var entity = mapper.map(household);
    var actualMembers = fetchReferencedMembers(entity);
    if (actualMembers.size() < entity.getMembers().size()) {
      log.warn("Some members referenced in [{}] are missing", household);
      throw new RuntimeException(
          "Some members from [%s] are missing from the database".formatted(household.members()));
    }

    entity.setMembers(actualMembers);
    return entity;
  }

  private HouseholdEntity createEntity(NewHousehold newHousehold) {
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

  private Set<MemberEntity> fetchReferencedMembers(HouseholdEntity household) {
    return StreamSupport.stream(
            membersRepository
                .findAllById(household.getMembers().stream().map(MemberEntity::getId).toList())
                .spliterator(),
            false)
        .collect(Collectors.toSet());
  }
}
