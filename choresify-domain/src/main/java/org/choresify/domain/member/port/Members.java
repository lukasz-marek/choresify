package org.choresify.domain.member.port;

import java.util.Optional;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface Members {
  Member insert(NewMember newMember);

  Optional<Member> findById(long memberId);

  Optional<Member> findByEmail(String email);
}
