package org.choresify.application.member.adapter.driving.rest;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class NewMemberDto {
  String nickname;
  String emailAddress;
}
