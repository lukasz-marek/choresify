package org.choresify.domain.member.usecase;

import org.choresify.domain.member.model.Member;

public interface UpdateMemberUseCase {
  Member execute(Member newValue);
}
