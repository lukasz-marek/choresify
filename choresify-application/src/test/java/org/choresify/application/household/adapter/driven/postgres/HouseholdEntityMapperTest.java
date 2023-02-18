package org.choresify.application.household.adapter.driven.postgres;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.choresify.application.household.adapter.driven.postgres.entity.HouseholdEntity;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.household.model.HouseholdMember;
import org.choresify.domain.household.model.NewHousehold;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class HouseholdEntityMapperTest {
  private final HouseholdEntityMapper tested = Mappers.getMapper(HouseholdEntityMapper.class);

  @Nested
  class NewHouseholdToEntity {
    @Test
    void allFieldsAreMapped() {
      // given
      var newHousehold =
          NewHousehold.builder()
              .name("a household")
              .members(Set.of(new HouseholdMember(5)))
              .build();

      // when
      var entity = tested.map(newHousehold);

      // then
      assertThat(entity.getId()).isNull();
      assertThat(entity.getVersion()).isEqualTo(0);
      assertThat(entity.getName()).isEqualTo("a household");
      assertThat(entity.getMembers())
          .containsExactlyInAnyOrder(MemberEntity.builder().id(5L).build());
    }
  }

  @Nested
  class EntityToHousehold {
    @Test
    void allFieldsAreMapped() {
      // given
      var entity =
          HouseholdEntity.builder()
              .name("a household")
              .id(21L)
              .version(37L)
              .members(Set.of(MemberEntity.builder().id(55L).build()))
              .build();

      // when
      var household = tested.map(entity);

      // then
      assertThat(household.id()).isEqualTo(21);
      assertThat(household.version()).isEqualTo(37);
      assertThat(household.name()).isEqualTo("a household");
      assertThat(household.members()).containsExactlyInAnyOrder(new HouseholdMember(55));
    }
  }
}
