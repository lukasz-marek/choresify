package org.choresify.domain.member.usecase;

import static io.vavr.control.Validation.invalid;
import static io.vavr.control.Validation.valid;
import static org.choresify.domain.error.Category.PRECONDITION;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.common.validation.Validator;
import org.choresify.domain.error.Failure;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultCreateMemberUseCase implements CreateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  private final Validator<NewMember> newMemberValidator;

  @Override
  public Either<Failure, Member> execute(NewMember newMember) {
    return newMemberValidator
        .validate(newMember)
        .flatMap(this::checkEmailNotInUse)
        .flatMap(this::createNewMember)
        .toEither();
  }

  private Validation<Failure, NewMember> checkEmailNotInUse(NewMember newMember) {
    if (isEmailInUse(newMember.getEmailAddress())) {
      log.info(
          "Member email [{}] already exists, insertion of [{}] canceled",
          newMember.getEmailAddress(),
          newMember);
      return invalid(Failure.of(PRECONDITION, "Email address already in use"));
    }
    return valid(newMember);
  }

  private Validation<Failure, Member> createNewMember(NewMember validMember) {
    log.info("Inserting [{}]", validMember);
    var member = members.insert(validMember);
    log.info("Finished insertion of [{}]", validMember);
    return valid(member);
  }

  private boolean isEmailInUse(String email) {
    var existingMemberWithSameEmail = members.findByEmail(email);
    return existingMemberWithSameEmail.isPresent();
  }
}
