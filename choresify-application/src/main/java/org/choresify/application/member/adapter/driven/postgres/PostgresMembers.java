package org.choresify.application.member.adapter.driven.postgres;

import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.util.List;
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
  public Validation<List<String>, Member> insert(NewMember newMember) {
    var newEntity = memberEntityMapper.map(newMember);
    return Try.of(() -> membersRepository.save(newEntity))
        .map(memberEntityMapper::map)
        .onFailure(throwable -> log.warn("Insertion of [{}] failed", newEntity, throwable))
        .toValidation()
        .mapError(throwable -> List.of(throwable.getMessage()));
  }
}
