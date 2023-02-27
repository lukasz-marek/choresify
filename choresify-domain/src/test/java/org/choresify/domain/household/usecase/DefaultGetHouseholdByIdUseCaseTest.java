package org.choresify.domain.household.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.choresify.domain.household.port.Households;
import org.choresify.fixtures.household.port.InMemoryHouseholds;
import org.junit.jupiter.api.Test;

class DefaultGetHouseholdByIdUseCaseTest {
  private final Households households = new InMemoryHouseholds();
  private final DefaultGetHouseholdByIdUseCase tested =
      new DefaultGetHouseholdByIdUseCase(households);

  @Test
  void returnsHouseholdWhenItExists() {
    // given
    var existingHousehold =
        households.insert(
            NewHousehold.builder()
                .name("a name")
                .members(Set.of(HouseholdMember.of(2137)))
                .build());

    // when
    var maybeHousehold = tested.execute(existingHousehold.id());

    // then
    assertThat(maybeHousehold).contains(existingHousehold);
  }

  @Test
  void returnsEmptyWhenHouseholdIsMissing() {
    // when
    var maybeHousehold = tested.execute(2137L);

    // then
    assertThat(maybeHousehold).isEmpty();
  }
}
