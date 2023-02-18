package org.choresify.domain.household.model;

import org.assertj.core.api.Assertions;
import org.choresify.domain.exception.InvariantViolationException;
import org.junit.jupiter.api.Test;

class HouseholdMemberTest {
  @Test
  void throwsWhenRoleIsNull() {
    // when
    var throwable =
        Assertions.catchThrowableOfType(
            () -> new HouseholdMember(1L, null), InvariantViolationException.class);

    // then
    Assertions.assertThat(throwable).hasMessageContaining("role");
  }

  @Test
  void canCreateNewInstanceWhenAllFieldsAreProvided() {
    // when
    var member = new HouseholdMember(1L, MemberRole.OWNER);

    // then
    Assertions.assertThat(member.memberId()).isEqualTo(1L);
    Assertions.assertThat(member.role()).isEqualTo(MemberRole.OWNER);
  }
}
