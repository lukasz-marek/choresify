package org.choresify.application.household.adapter.driven.postgres;

import java.util.Optional;
import org.choresify.application.household.adapter.driven.postgres.entity.HouseholdEntity;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
abstract class HouseholdEntityMapper {

  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "version", constant = "0L")
  public abstract HouseholdEntity map(NewHousehold newHousehold);

  public abstract Household map(HouseholdEntity entity);

  public abstract HouseholdEntity map(Household household);

  protected HouseholdMember map(MemberEntity entity) {
    return Optional.ofNullable(entity)
        .filter(anEntity -> anEntity.getId() != null)
        .map(anEntity -> new HouseholdMember(entity.getId()))
        .orElse(null);
  }

  protected MemberEntity map(HouseholdMember member) {
    return Optional.ofNullable(member)
        .map(aMember -> MemberEntity.builder().id(member.memberId()).build())
        .orElse(null);
  }
}
