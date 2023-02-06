package org.choresify.fixtures.member.port;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

public final class InMemoryMembers implements Members {
  private final Map<Long, Member> storage = new LinkedHashMap<>();
  private long nextId = 1L;

  @Override
  public Member insert(NewMember newMember) {
    var member =
        Member.builder()
            .nickname(newMember.getNickname())
            .emailAddress(newMember.getEmailAddress())
            .id(nextId++)
            .version(1)
            .build();
    storage.put(member.getId(), member);
    return member;
  }

  @Override
  public Optional<Member> findById(long memberId) {
    return Optional.ofNullable(storage.get(memberId));
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    return storage.values().stream()
        .filter(member -> member.getEmailAddress().equals(email))
        .findAny();
  }
}
