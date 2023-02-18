package org.choresify.fixtures.member.port;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
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
            .nickname(newMember.nickname())
            .emailAddress(newMember.emailAddress())
            .id(nextId++)
            .build();
    storage.put(member.id(), member);
    return member;
  }

  @Override
  public Member save(Member member) {
    var newRevision =
        Member.builder()
            .id(member.id())
            .nickname(member.nickname())
            .emailAddress(member.emailAddress())
            .build();
    storage.put(newRevision.id(), newRevision);
    return newRevision;
  }

  @Override
  public Optional<Member> updateWithOptimisticLock(Member member) {
    var existing = storage.get(member.id());
    if (existing == null || existing.version() != member.version()) {
      return Optional.empty();
    }
    var newRevision = member.toBuilder().version(member.version() + 1).build();
    storage.put(newRevision.id(), newRevision);
    return Optional.of(newRevision);
  }

  @Override
  public Optional<Member> findById(long memberId) {
    return Optional.ofNullable(storage.get(memberId));
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    return storage.values().stream()
        .filter(member -> member.emailAddress().equals(email))
        .findAny();
  }

  @Override
  public Map<Long, Member> findById(Iterable<Long> memberIds) {
    var requestedIds = new HashSet<Long>();
    memberIds.forEach(requestedIds::add);
    return storage.values().stream()
        .filter(member -> requestedIds.contains(member.id()))
        .collect(Collectors.toMap(Member::id, Function.identity()));
  }
}
