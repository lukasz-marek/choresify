package org.choresify.domain.member.usecase;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static org.choresify.domain.error.Category.VALIDATION;

import io.vavr.control.Validation;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validations;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.error.Failure;
import org.choresify.domain.error.FailureDetails;
import org.choresify.domain.member.model.NewMember;

@Slf4j
public final class NewMemberValidator implements Validator<NewMember> {
  @Override
  public Validation<Failure, NewMember> validate(NewMember newMember) {
    log.info("Validating [{}]", newMember);
    return validateNotNull(newMember).flatMap(nonNullMember -> validateFields(newMember));
  }

  private Validation<Failure, NewMember> validateFields(NewMember newMember) {
    var validationResults =
        Validations.joining(
            newMember, this::validateNicknameNotNull, this::validateEmailAddressNotNull);
    return validationResults.mapError(Failure::of);
  }

  private Validation<Failure, NewMember> validateNotNull(NewMember newMember) {
    if (newMember == null) {
      log.warn("NewMember must not be null");
      return invalid(Failure.of(VALIDATION, "NewMember must not be null"));
    }
    log.info("[{}] - not-null validation successful", newMember);
    return valid(newMember);
  }

  private Validation<FailureDetails, NewMember> validateNicknameNotNull(NewMember newMember) {
    if (newMember.getNickname() == null) {
      log.warn("[{}] - nickname must not be null", newMember);
      return invalid(FailureDetails.of(VALIDATION, "nickname must not be null"));
    }
    log.info("[{}] - nickname validation successful", newMember);
    return valid(newMember);
  }

  private Validation<FailureDetails, NewMember> validateEmailAddressNotNull(NewMember newMember) {
    if (newMember.getEmailAddress() == null) {
      log.warn("[{}] - email address must not be null", newMember);
      return invalid(FailureDetails.of(VALIDATION, "email address must not be null"));
    }
    log.info("[{}] - email address validation successful", newMember);
    return valid(newMember);
  }
}
