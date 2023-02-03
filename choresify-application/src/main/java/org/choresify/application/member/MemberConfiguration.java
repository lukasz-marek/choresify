package org.choresify.application.member;

import org.choresify.application.member.adapter.driven.postgres.transaction.TransactionalCreateMemberUseCase;
import org.choresify.domain.member.port.Members;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.choresify.domain.member.usecase.DefaultCreateMemberUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MemberConfiguration {

  @Bean
  CreateMemberUseCase createMemberUseCase(Members members) {
    return new TransactionalCreateMemberUseCase(new DefaultCreateMemberUseCase(members));
  }
}
