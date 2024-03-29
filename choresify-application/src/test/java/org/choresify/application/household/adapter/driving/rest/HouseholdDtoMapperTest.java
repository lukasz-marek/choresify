package org.choresify.application.household.adapter.driving.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.model.HouseholdMember;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class HouseholdDtoMapperTest {
  private final HouseholdDtoMapper tested = Mappers.getMapper(HouseholdDtoMapper.class);

  @Nested
  class NewHouseholdDtoToNewHousehold {
    @Test
    void allFieldsAreMapped() {
      // given
      var dto =
          NewHouseholdDto.builder()
              .name("Household name")
              .members(Set.of(new HouseholdMemberDto(1), new HouseholdMemberDto(5)))
              .build();

      // when
      var newHousehold = tested.map(dto);

      // then
      assertThat(newHousehold.name()).isEqualTo("Household name");
      assertThat(newHousehold.members())
          .containsExactlyInAnyOrder(HouseholdMember.of(1), HouseholdMember.of(5));
    }
  }

  @Nested
  class HouseholdToHouseholdDto {
    @Test
    void allFieldsAreMapped() {
      // given
      var household =
          Household.builder()
              .id(21)
              .version(37)
              .name("Household name")
              .members(Set.of(HouseholdMember.of(1), HouseholdMember.of(5)))
              .build();

      // when
      var dto = tested.map(household);

      // then
      assertThat(dto.name()).isEqualTo("Household name");
      assertThat(dto.id()).isEqualTo(21);
      assertThat(dto.version()).isEqualTo(37);
      assertThat(dto.members())
          .containsExactlyInAnyOrder(new HouseholdMemberDto(1), new HouseholdMemberDto(5));
    }
  }

  @Nested
  class HouseholdDtoToHousehold {
    @Test
    void allFieldsAreMapped() {
      // given
      var dto =
          HouseholdDto.builder()
              .id(21)
              .version(37)
              .name("Household name")
              .members(Set.of(new HouseholdMemberDto(1), new HouseholdMemberDto(5)))
              .build();

      // when
      var household = tested.map(dto);

      // then
      assertThat(household.name()).isEqualTo("Household name");
      assertThat(household.id()).isEqualTo(21);
      assertThat(household.version()).isEqualTo(37);
      assertThat(household.members())
          .containsExactlyInAnyOrder(HouseholdMember.of(1), HouseholdMember.of(5));
    }
  }
}
