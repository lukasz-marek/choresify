package org.choresify.application.member.adapter.driving.rest;

import lombok.RequiredArgsConstructor;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
final class MemberController {
  private final CreateMemberUseCase createMemberUseCase;
  private final MemberDtoMapper memberDtoMapper;

  @PostMapping
  public ResponseEntity<MemberDto> postMember(@RequestBody NewMemberDto newMemberDto) {
    var newMember = memberDtoMapper.map(newMemberDto);
    var member = createMemberUseCase.execute(newMember).get();
    var memberDto = memberDtoMapper.map(member);
    return ResponseEntity.status(HttpStatus.CREATED).body(memberDto);
  }
}
