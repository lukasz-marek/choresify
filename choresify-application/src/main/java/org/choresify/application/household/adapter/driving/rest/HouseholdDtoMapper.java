package org.choresify.application.household.adapter.driving.rest;

import java.util.Optional;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
abstract class HouseholdDtoMapper {
  abstract NewHousehold map(NewHouseholdDto dto);

  abstract HouseholdDto map(Household household);

  abstract Household map(HouseholdDto household);

  protected HouseholdMember map(HouseholdMemberDto that) {
    return Optional.ofNullable(that).map(dto -> HouseholdMember.of(dto.memberId())).orElse(null);
  }
}
