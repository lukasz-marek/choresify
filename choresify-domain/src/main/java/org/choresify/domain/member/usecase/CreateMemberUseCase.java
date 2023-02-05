package org.choresify.domain.member.usecase;

import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface CreateMemberUseCase {
  Member execute(NewMember newMember);
}
