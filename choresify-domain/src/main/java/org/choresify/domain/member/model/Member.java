package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.exception.Invariant;

@Builder
public record Member(long id, String nickname, String emailAddress) {
  public Member {
    Invariant.assertTrue(nickname != null, "nickname must not be null");
    Invariant.assertTrue(emailAddress != null, "emailAddress must not be null");
  }
}
