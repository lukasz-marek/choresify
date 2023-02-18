package org.choresify.application.household;

import org.choresify.domain.household.port.Households;
import org.choresify.domain.household.usecase.CreateHouseholdUseCase;
import org.choresify.domain.household.usecase.DefaultCreateHouseholdUseCase;
import org.choresify.domain.member.port.Members;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class HouseholdConfiguration {
  @Bean
  CreateHouseholdUseCase createHouseholdUseCase(Members members, Households households) {
    return new DefaultCreateHouseholdUseCase(members, households);
  }
}
