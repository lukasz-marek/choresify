package org.choresify.application.member.adapter.driving.rest;

import lombok.Builder;

@Builder
record NewMemberDto(String nickname, String emailAddress) {}
