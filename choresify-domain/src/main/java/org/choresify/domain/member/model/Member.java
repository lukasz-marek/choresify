package org.choresify.domain.member.model;

import lombok.Builder;
import org.choresify.domain.common.sanitation.EmailAddressSanitizer;
import org.choresify.domain.common.sanitation.NameSanitizer;
import org.choresify.domain.exception.Invariants;

@Builder(toBuilder = true)
public record Member(long id, String nickname, String emailAddress, long version) {
  public Member {
    Invariants.requireNonNull(nickname, "nickname");
    Invariants.requireNonNull(emailAddress, "emailAddress");

    emailAddress = EmailAddressSanitizer.sanitize(emailAddress);
    nickname = NameSanitizer.sanitize(nickname);
  }
}
