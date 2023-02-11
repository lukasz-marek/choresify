package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.exception.Invariant;

@Builder
public record NewMember(String nickname, String emailAddress) {
  public NewMember {
    Invariant.assertTrue(nickname != null, "nickname must not be null");
    Invariant.assertTrue(emailAddress != null, "emailAddress must not be null");
  }
}
