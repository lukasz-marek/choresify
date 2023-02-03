package org.choresify.domain.member.usecase;

import io.vavr.control.Validation;
import java.util.List;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface CreateMemberUseCase {
  Validation<List<String>, Member> execute(NewMember newMember);
}
