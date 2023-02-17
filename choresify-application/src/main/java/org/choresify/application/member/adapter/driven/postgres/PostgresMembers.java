package org.choresify.application.member.adapter.driven.postgres;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.member.adapter.driven.postgres.entity.MemberEntity;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.MANDATORY)
@RequiredArgsConstructor
@Slf4j
class PostgresMembers implements Members {
  private final MembersRepository membersRepository;
  private final MemberEntityMapper memberEntityMapper;

  @Override
  public Member insert(@NonNull NewMember newMember) {
    var newEntity = memberEntityMapper.map(newMember);
    log.info("Inserting [{}] into database", newEntity);
    var created = membersRepository.save(newEntity);
    log.info("Successfully inserted [{}] into database", created);
    return memberEntityMapper.map(created);
  }

  @Override
  public Member save(@NonNull Member member) {
    var forUpdate = memberEntityMapper.map(member);
    log.info("Saving [{}] into database", forUpdate);
    var created = membersRepository.save(forUpdate);
    log.info("Successfully saved [{}] into database", created);
    return memberEntityMapper.map(created);
  }

  /*
  Optimistic lock requires at least repeatable reads to work correctly.
   */
  @Override
  public Optional<Member> updateWithOptimisticLock(@NonNull Member member) {
    if (isOptimisticLockValid(member)) {
      log.info("Optimistic lock is valid for [{}] - performing update", member);
      return Optional.of(performUpdate(member)).map(memberEntityMapper::map);
    }
    log.info("Optimistic lock is invalid for [{}] - update will not be performed", member);
    return Optional.empty();
  }

  private MemberEntity performUpdate(Member member) {
    var entityForUpdate = memberEntityMapper.map(member);
    entityForUpdate.setVersion(entityForUpdate.getVersion() + 1);
    return membersRepository.save(entityForUpdate);
  }

  private boolean isOptimisticLockValid(Member member) {
    var existing = membersRepository.findById(member.id());
    return existing
        .map(currentRevision -> currentRevision.getVersion() == member.version())
        .orElse(false);
  }

  @Override
  public Optional<Member> findById(long memberId) {
    return membersRepository.findById(memberId).map(memberEntityMapper::map);
  }

  @Override
  public Optional<Member> findByEmail(@NonNull String email) {
    return membersRepository.findByEmailAddress(email).map(memberEntityMapper::map);
  }
}
