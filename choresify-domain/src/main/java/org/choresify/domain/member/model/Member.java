package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.exception.InvariantViolationException;

@Builder
public record Member(long id, String nickname, String emailAddress) {
  public Member {
    if (nickname == null) throw new InvariantViolationException("nickname must not be null");
    if (emailAddress == null)
      throw new InvariantViolationException("emailAddress must not be null");
  }
}
