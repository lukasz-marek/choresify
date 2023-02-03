package org.choresify.domain.member.model;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewMember {

  String nickname;
  String emailAddress;
}
