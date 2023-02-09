package org.choresify.domain.member.validation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.exception.DomainException.ValidationException;
import org.choresify.domain.member.model.NewMember;

@Slf4j
public final class NewMemberValidator implements Validator<NewMember> {
  @Override
  public void validate(@NonNull NewMember newMember) {
    log.info("Validating [{}]", newMember);
    validateNicknameNotNull(newMember);
    validateEmailAddressNotNull(newMember);
  }

  private void validateNicknameNotNull(NewMember newMember) {
    if (newMember.getNickname() == null) {
      log.warn("[{}] - nickname must not be null", newMember);
      throw new ValidationException("nickname must not be null");
    }
    log.info("[{}] - nickname validation successful", newMember);
  }

  private void validateEmailAddressNotNull(NewMember newMember) {
    if (newMember.getEmailAddress() == null) {
      log.warn("[{}] - email address must not be null", newMember);
      throw new ValidationException("email address must not be null");
    }
    log.info("[{}] - email address validation successful", newMember);
  }
}
