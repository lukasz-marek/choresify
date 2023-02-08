package org.choresify.domain.member.usecase;

import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.exception.DomainException.ValidationException;
import org.choresify.domain.member.model.Member;

@Slf4j
public final class MemberValidator implements Validator<Member> {
  @Override
  public void validate(Member member) {
    log.info("Validating [{}]", member);
    validateNotNull(member);
    validateNicknameNotNull(member);
    validateEmailAddressNotNull(member);
  }

  private void validateNotNull(Member member) {
    if (member == null) {
      log.warn("Member must not be null");
      throw new ValidationException("Member must not be null");
    }
    log.info("[{}] - not-null validation successful", member);
  }

  private void validateNicknameNotNull(Member member) {
    if (member.getNickname() == null) {
      log.warn("[{}] - nickname must not be null", member);
      throw new ValidationException("nickname must not be null");
    }
    log.info("[{}] - nickname validation successful", member);
  }

  private void validateEmailAddressNotNull(Member member) {
    if (member.getEmailAddress() == null) {
      log.warn("[{}] - email address must not be null", member);
      throw new ValidationException("email address must not be null");
    }
    log.info("[{}] - email address validation successful", member);
  }
}
