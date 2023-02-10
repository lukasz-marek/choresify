package org.choresify.domain.member.model;

import lombok.Builder;

@Builder
public record Member(long id, String nickname, String emailAddress) {
}
