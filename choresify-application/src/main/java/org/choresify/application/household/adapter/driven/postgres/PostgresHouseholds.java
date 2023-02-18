package org.choresify.application.household.adapter.driven.postgres;

import java.util.HashSet;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.household.adapter.driven.postgres.entity.HouseholdEntity;
import org.choresify.application.member.adapter.driven.postgres.MembersRepository;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
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
  private final MembersRepository membersRepository;
  private final HouseholdEntityMapper mapper;

  @Override
  public Optional<Household> insert(@NonNull NewHousehold newHousehold) {
    var entityForInsert = buildEntity(newHousehold);
    return entityForInsert.map(householdRepository::save).map(mapper::map);
  }

  private Optional<HouseholdEntity> buildEntity(NewHousehold newHousehold) {
    var actualMembers = fetchReferencedMembers(newHousehold);
    if (actualMembers.size() < newHousehold.members().size()) {
      log.info("Some members from [{}] are missing - insertion rejected", newHousehold);
      return Optional.empty();
    }

    var entityForInsert = mapper.map(newHousehold);
    entityForInsert.setMembers(actualMembers);
    log.info("All members from [{}] are present - proceeding with insertion", newHousehold);
    return Optional.of(entityForInsert);
  }

  private HashSet<MemberEntity> fetchReferencedMembers(NewHousehold newHousehold) {
    var members =
        membersRepository.findAllById(
            newHousehold.members().stream().map(HouseholdMember::memberId).toList());
    var actualMembers = new HashSet<MemberEntity>();
    members.forEach(actualMembers::add);
    return actualMembers;
  }
}
