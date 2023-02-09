package org.choresify.application.member.adapter.driven.postgres;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  @Override
  public Optional<Member> findById(long memberId) {
    return membersRepository.findById(memberId).map(memberEntityMapper::map);
  }

  @Override
  public Optional<Member> findByEmail(@NonNull String email) {
    return membersRepository.findByEmailAddress(email).map(memberEntityMapper::map);
  }
}
