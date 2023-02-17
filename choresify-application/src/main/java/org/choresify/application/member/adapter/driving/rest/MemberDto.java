package org.choresify.application.member.adapter.driving.rest;

import lombok.Builder;

@Builder
record MemberDto(long id, String nickname, String emailAddress, long version) {}
