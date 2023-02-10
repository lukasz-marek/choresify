package org.choresify.domain.member.model;

import lombok.Builder;

@Builder
public record NewMember(String nickname, String emailAddress) {
}
