package org.choresify.application.member.adapter.driving.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import io.vavr.control.Validation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.transaction.TransactionalRunner;
import org.choresify.domain.member.usecase.CreateMemberUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
  private final MemberDtoMapper memberDtoMapper;

  @PostMapping
  public ResponseEntity<MemberDto> postMember(@RequestBody NewMemberDto newMemberDto) {
    var newMember = memberDtoMapper.map(newMemberDto);
    var creationResult = transactionalRunner.execute(() -> createMemberUseCase.execute(newMember));

    if (creationResult.isInvalid()) {
      log.info(
          "Failed to create new member [{}], reasons [{}]",
          newMemberDto,
          creationResult.getError());
      handleFailure(newMemberDto, creationResult);
    }

    var memberDto = memberDtoMapper.map(creationResult.get());
    return ResponseEntity.status(CREATED).body(memberDto);
  }

  private void handleFailure(
      NewMemberDto newMemberDto, Validation<List<String>, ?> creationResult) {
    throw new ResponseStatusException(
        BAD_REQUEST, "Request failed: %s".formatted(formatError(creationResult.getError())));
  }

  private String formatError(List<String> errors) {
    return String.join(", ", errors);
  }
}
