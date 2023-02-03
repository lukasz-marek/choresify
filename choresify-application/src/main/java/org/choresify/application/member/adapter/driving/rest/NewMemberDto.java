package org.choresify.application.member.adapter.driving.rest;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;
import org.immutables.value.Value.Style.ImplementationVisibility;

@Value.Immutable
@Value.Style(visibility = ImplementationVisibility.PACKAGE)
@JsonSerialize(as = ImmutableNewMemberDto.class)
@JsonDeserialize(as = ImmutableNewMemberDto.class)
public interface NewMemberDto {
  String getNickname();

  String getEmailAddress();
}
