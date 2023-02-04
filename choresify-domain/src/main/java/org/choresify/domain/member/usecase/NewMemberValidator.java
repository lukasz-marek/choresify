package org.choresify.domain.member.usecase;

import io.vavr.control.Validation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validations;
import org.choresify.domain.member.model.NewMember;

@Slf4j
public class NewMemberValidator {
  public Validation<List<String>, NewMember> validate(NewMember newMember) {
    log.info("Validating [{}]", newMember);
    return validateNotNull(newMember).flatMap(nonNullMember -> validateFields(newMember));
  }

  private Validation<List<String>, NewMember> validateFields(NewMember newMember) {
    return Validations.joining(
        newMember, this::validateNicknameNotNull, this::validateEmailAddressNotNull);
  }

  private Validation<List<String>, NewMember> validateNotNull(NewMember newMember) {
    if (newMember == null) {
      log.warn("NewMember must not be null");
      return Validation.invalid(List.of("NewMember must not be null"));
    }
    log.info("[{}] - not-null validation successful", newMember);
    return Validation.valid(newMember);
  }

  private Validation<String, NewMember> validateNicknameNotNull(NewMember newMember) {
    if (newMember.getNickname() == null) {
      log.warn("[{}] - nickname must not be null", newMember);
      return Validation.invalid("nickname must not be null");
    }
    log.info("[{}] - nickname validation successful", newMember);
    return Validation.valid(newMember);
  }

  private Validation<String, NewMember> validateEmailAddressNotNull(NewMember newMember) {
    if (newMember.getEmailAddress() == null) {
      log.warn("[{}] - email address must not be null", newMember);
      return Validation.invalid("email address must not be null");
    }
    log.info("[{}] - email address validation successful", newMember);
    return Validation.valid(newMember);
  }
}
