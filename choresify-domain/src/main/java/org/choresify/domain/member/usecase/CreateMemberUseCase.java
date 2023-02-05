package org.choresify.domain.member.usecase;

import io.vavr.control.Either;
import org.choresify.domain.error.Failure;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface CreateMemberUseCase {
  Either<Failure, Member> execute(NewMember newMember);
}
