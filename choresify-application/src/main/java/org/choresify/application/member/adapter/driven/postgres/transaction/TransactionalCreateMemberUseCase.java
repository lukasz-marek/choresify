package org.choresify.application.member.adapter.driven.postgres.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class TransactionalCreateMemberUseCase implements CreateMemberUseCase {
  private final CreateMemberUseCase wrapped;

  @Override
  public Member execute(NewMember newMember) {
    log.debug("Creating a new member=[{}] in transaction", newMember);
    try {
      return wrapped.execute(newMember);
    } finally {
      log.debug("Transactional creation of new member=[{}] finished", newMember);
    }
  }
}
