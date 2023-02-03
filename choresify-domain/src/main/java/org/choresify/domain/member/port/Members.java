package org.choresify.domain.member.port;

import io.vavr.control.Validation;
import java.util.List;
import org.choresify.domain.member.model.Member;
import org.choresify.domain.member.model.NewMember;

public interface Members {
  Validation<List<String>, Member> insert(NewMember newMember);
}
