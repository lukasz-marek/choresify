package org.choresify.domain.member.usecase;

import java.util.Optional;
import org.choresify.domain.member.model.Member;

public interface GetMemberByIdUseCase {
  Optional<Member> execute(long memberId);
}
