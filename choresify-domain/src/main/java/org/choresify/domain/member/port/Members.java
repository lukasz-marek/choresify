package org.choresify.domain.member.port;

import java.util.Map;
import java.util.Optional;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface Members {
  Member insert(NewMember newMember);

  Member save(Member member);

  Optional<Member> updateWithOptimisticLock(Member member);

  Optional<Member> findById(long memberId);

  Optional<Member> findByEmail(String email);

  Map<Long, Member> findById(Iterable<Long> memberIds);
}
