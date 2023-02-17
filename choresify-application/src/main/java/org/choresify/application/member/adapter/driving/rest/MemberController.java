package org.choresify.application.member.adapter.driving.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.choresify.domain.member.usecase.GetMemberByIdUseCase;
import org.choresify.domain.member.usecase.UpdateMemberUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Slf4j
final class MemberController {
  private final TransactionalRunner transactionalRunner;
  private final CreateMemberUseCase createMemberUseCase;
  private final GetMemberByIdUseCase getMemberByIdUseCase;
  private final UpdateMemberUseCase updateMemberUseCase;
  private final MemberDtoMapper memberDtoMapper;

  private static void checkMemberId(MemberDto memberDto, long memberId) {
    if (!Objects.equals(memberId, memberDto.id())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ambiguous resource id: found %s in body and %s in uri"
              .formatted(memberDto.id(), memberId));
    }
  }

  @PostMapping
  public ResponseEntity<MemberDto> postMember(@RequestBody NewMemberDto newMemberDto) {
    var newMember = memberDtoMapper.map(newMemberDto);
    var creationResult = transactionalRunner.execute(() -> createMemberUseCase.execute(newMember));
    var memberDto = memberDtoMapper.map(creationResult);
    return ResponseEntity.status(CREATED).body(memberDto);
  }

  @GetMapping("/{memberId}")
  public ResponseEntity<MemberDto> getMember(@PathVariable("memberId") long memberId) {
    var existingMember = transactionalRunner.execute(() -> getMemberByIdUseCase.execute(memberId));
    return existingMember
        .map(memberDtoMapper::map)
        .map(memberDto -> ResponseEntity.status(OK).body(memberDto))
        .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
  }

  @PutMapping("/{memberId}")
  public ResponseEntity<MemberDto> putMember(
      @RequestBody MemberDto memberDto, @PathVariable("memberId") long memberId) {
    checkMemberId(memberDto, memberId);
    var updateValue = memberDtoMapper.map(memberDto);
    var updateResult = transactionalRunner.execute(() -> updateMemberUseCase.execute(updateValue));
    var responseDto = memberDtoMapper.map(updateResult);
    return ResponseEntity.ok(responseDto);
  }
}
