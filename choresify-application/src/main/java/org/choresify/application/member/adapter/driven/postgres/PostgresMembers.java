package org.choresify.application.member.adapter.driven.postgres;

import java.util.Optional;
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
  public Member insert(NewMember newMember) {
    var newEntity = memberEntityMapper.map(newMember);
    log.info("Inserting [{}] into database", newEntity);
    var created = membersRepository.save(newEntity);
    log.info("Successfully inserted [{}] into database", created);
    return memberEntityMapper.map(created);
  }

  @Override
  public Optional<Member> get(long memberId) {
    return Optional.empty();
  }

  @Override
  public Optional<Member> findByEmail(String email) {
    return membersRepository.findByEmailAddress(email).map(memberEntityMapper::map);
  }
}
