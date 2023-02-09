package org.choresify.application.member;

import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.choresify.domain.member.usecase.DefaultCreateMemberUseCase;
import org.choresify.domain.member.usecase.DefaultGetMemberByIdUseCase;
import org.choresify.domain.member.usecase.DefaultUpdateMemberUseCase;
import org.choresify.domain.member.usecase.GetMemberByIdUseCase;
import org.choresify.domain.member.validation.MemberValidator;
import org.choresify.domain.member.validation.NewMemberValidator;
import org.choresify.domain.member.usecase.UpdateMemberUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class MemberConfiguration {

  @Bean
  CreateMemberUseCase createMemberUseCase(
      Members members, Validator<NewMember> newMemberValidator) {
    return new DefaultCreateMemberUseCase(members, newMemberValidator);
  }

  @Bean
  GetMemberByIdUseCase getMemberByIdUseCase(Members members) {
    return new DefaultGetMemberByIdUseCase(members);
  }

  @Bean
  UpdateMemberUseCase updateMemberUseCase(Members members, Validator<Member> memberValidator) {
    return new DefaultUpdateMemberUseCase(members, memberValidator);
  }

  @Bean
  Validator<NewMember> newMemberValidator() {
    return new NewMemberValidator();
  }

  @Bean
  Validator<Member> memberValidator() {
    return new MemberValidator();
  }
}
