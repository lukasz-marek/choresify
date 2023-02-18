package org.choresify.application.household.adapter.driving.rest;

import org.choresify.domain.household.model.NewHousehold;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;

@Mapper(componentModel = ComponentModel.SPRING)
interface HouseholdDtoMapper {
  NewHousehold map(NewHouseholdDto dto);
}
