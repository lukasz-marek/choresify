package org.choresify.domain.member.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
  public Validation<List<String>, Member> execute(NewMember newMember) {
    return newMemberValidator.validate(newMember).flatMap(this::insertMember);
  }

  private Validation<List<String>, Member> insertMember(NewMember validMember) {
    log.info("Validations successful - inserting [{}]", validMember);
    var member = members.insert(validMember);
    log.info("Finished insertion of [{}]", validMember);
    return member;
  }
}
