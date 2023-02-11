package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.exception.InvariantViolationException;

@Builder
public record NewMember(String nickname, String emailAddress) {
  public NewMember {
    if (nickname == null) throw new InvariantViolationException("nickname must not be null");
    if (emailAddress == null)
      throw new InvariantViolationException("emailAddress must not be null");
  }
}
