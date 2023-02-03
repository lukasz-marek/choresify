package org.choresify.domain.member.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Member {
  long id;
  String nickname;
  String emailAddress;
  long version;
}
