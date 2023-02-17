package org.choresify.application.member.adapter.driving.rest;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
class MemberDto {
  long id;
  String nickname;
  String emailAddress;

  long version;
}
