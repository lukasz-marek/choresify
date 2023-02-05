package org.choresify.application.member.adapter.driving.rest;

import static org.springframework.http.HttpStatus.CREATED;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.transaction.TransactionalRunner;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
final class MemberController {
  private final TransactionalRunner transactionalRunner;
  private final CreateMemberUseCase createMemberUseCase;
  private final MemberDtoMapper memberDtoMapper;

  @PostMapping
  public ResponseEntity<MemberDto> postMember(@RequestBody NewMemberDto newMemberDto) {
    var newMember = memberDtoMapper.map(newMemberDto);
    var creationResult = transactionalRunner.execute(() -> createMemberUseCase.execute(newMember));
    var memberDto = memberDtoMapper.map(creationResult);
    return ResponseEntity.status(CREATED).body(memberDto);
  }
}
