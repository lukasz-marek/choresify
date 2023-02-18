package org.choresify.application.household.adapter.driving.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.household.usecase.CreateHouseholdUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/households")
@RequiredArgsConstructor
@Slf4j
final class HouseholdController {
  private final TransactionalRunner transactionalRunner;
  private final HouseholdDtoMapper householdDtoMapper;
  private final CreateHouseholdUseCase createHouseholdUseCase;

  @PostMapping
  ResponseEntity<HouseholdDto> createHousehold(@RequestBody NewHouseholdDto newHouseholdDto) {
    var newHousehold = householdDtoMapper.map(newHouseholdDto);
    var createdHousehold =
        transactionalRunner.execute(() -> createHouseholdUseCase.execute(newHousehold));
    var responseDto = householdDtoMapper.map(createdHousehold);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }
}
