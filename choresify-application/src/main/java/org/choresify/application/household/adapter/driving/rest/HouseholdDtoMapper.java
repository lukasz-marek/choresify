package org.choresify.application.household.adapter.driving.rest;

import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.NewHousehold;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR)
interface HouseholdDtoMapper {
  NewHousehold map(NewHouseholdDto dto);

  HouseholdDto map(Household household);
}
