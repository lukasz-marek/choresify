package org.choresify.application.member;

import org.choresify.domain.member.port.Members;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.choresify.domain.member.usecase.DefaultCreateMemberUseCase;
import org.choresify.domain.member.usecase.DefaultGetMemberByIdUseCase;
import org.choresify.domain.member.usecase.DefaultUpdateMemberUseCase;
import org.choresify.domain.member.usecase.GetMemberByIdUseCase;
import org.choresify.domain.member.usecase.UpdateMemberUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MemberConfiguration {

  @Bean
  CreateMemberUseCase createMemberUseCase(Members members) {
    return new DefaultCreateMemberUseCase(members);
  }

  @Bean
  GetMemberByIdUseCase getMemberByIdUseCase(Members members) {
    return new DefaultGetMemberByIdUseCase(members);
  }

  @Bean
  UpdateMemberUseCase updateMemberUseCase(Members members) {
    return new DefaultUpdateMemberUseCase(members);
  }
}
