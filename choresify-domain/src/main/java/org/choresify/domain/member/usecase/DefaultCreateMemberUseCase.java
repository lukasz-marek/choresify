package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.vavr.control.Either;
import io.vavr.control.Validation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;
import org.choresify.domain.member.port.Members;

@RequiredArgsConstructor
@Slf4j
public final class DefaultCreateMemberUseCase implements CreateMemberUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Members members;

  private final NewMemberValidator newMemberValidator;

  @Override
  public Either<List<String>, Member> execute(NewMember newMember) {
    return newMemberValidator.validate(newMember).flatMap(this::insertMember).toEither();
  }

  private Validation<List<String>, Member> insertMember(NewMember validMember) {
    log.info("Validations successful - inserting [{}]", validMember);
    if (isEmailInUse(validMember.getEmailAddress())) {
      log.info(
          "Member email [{}] already exists, insertion of [{}] canceled",
          validMember.getEmailAddress(),
          validMember);
      return Validation.invalid(List.of("Email address already in use"));
    }
    var member = members.insert(validMember);
    log.info("Finished insertion of [{}]", validMember);
    return Validation.valid(member);
  }

  private boolean isEmailInUse(String email) {
    var existingMemberWithSameEmail = members.findByEmail(email);
    return existingMemberWithSameEmail.isPresent();
  }
}
