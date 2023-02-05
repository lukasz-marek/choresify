package org.choresify.application.member.adapter.driving.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

import io.vavr.control.Either;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.transaction.TransactionalRunner;
import org.choresify.domain.error.Failure;
import org.choresify.domain.error.FailureDetails;
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

    if (creationResult.isLeft()) {
      log.info(
          "Failed to create new member [{}], reasons [{}]", newMemberDto, creationResult.getLeft());
      handleFailure(creationResult);
    }

    var memberDto = memberDtoMapper.map(creationResult.get());
    return ResponseEntity.status(CREATED).body(memberDto);
  }

  private void handleFailure(Either<Failure, ?> creationResult) {
    var errorMessage = formatErrorMessage(creationResult.getLeft());
    throw new ResponseStatusException(BAD_REQUEST, "Request failed: %s".formatted(errorMessage));
  }

  private String formatErrorMessage(Failure failure) {
    return failure.getFailureDetails().stream()
        .map(FailureDetails::getMessage)
        .collect(Collectors.joining(", "));
  }
}
