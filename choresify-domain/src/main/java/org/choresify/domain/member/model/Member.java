package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.exception.Invariants;

@Builder
public record Member(long id, String nickname, String emailAddress, long version) {
  public Member {
    Invariants.requireNonNull(nickname, "nickname");
    Invariants.requireNonNull(emailAddress, "emailAddress");
  }
}
