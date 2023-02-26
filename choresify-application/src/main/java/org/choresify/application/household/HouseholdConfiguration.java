package org.choresify.application.household;

import org.choresify.domain.household.port.Households;
import org.choresify.domain.household.usecase.CreateHouseholdUseCase;
import org.choresify.domain.household.usecase.DefaultCreateHouseholdUseCase;
import org.choresify.domain.household.usecase.DefaultGetHouseholdByIdUseCase;
import org.choresify.domain.household.usecase.DefaultUpdateHouseholdUseCase;
import org.choresify.domain.household.usecase.GetHouseholdByIdUseCase;
import org.choresify.domain.household.usecase.UpdateHouseholdUseCase;
import org.choresify.domain.member.port.Members;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HouseholdConfiguration {
  @Bean
  CreateHouseholdUseCase createHouseholdUseCase(Members members, Households households) {
    return new DefaultCreateHouseholdUseCase(members, households);
  }

  @Bean
  GetHouseholdByIdUseCase getHouseholdByIdUseCase(Households households) {
    return new DefaultGetHouseholdByIdUseCase(households);
  }

  @Bean
  UpdateHouseholdUseCase getUpdateHouseholdUseCase(Members members, Households households) {
    return new DefaultUpdateHouseholdUseCase(members, households);
  }
}
