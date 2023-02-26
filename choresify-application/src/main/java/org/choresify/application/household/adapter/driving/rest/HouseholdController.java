package org.choresify.application.household.adapter.driving.rest;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.application.common.transaction.TransactionalRunner;
import org.choresify.domain.household.usecase.CreateHouseholdUseCase;
import org.choresify.domain.household.usecase.GetHouseholdByIdUseCase;
import org.choresify.domain.household.usecase.UpdateHouseholdUseCase;
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
@RequestMapping("/api/v1/households")
@RequiredArgsConstructor
@Slf4j
final class HouseholdController {
  private final TransactionalRunner transactionalRunner;
  private final HouseholdDtoMapper householdDtoMapper;
  private final CreateHouseholdUseCase createHouseholdUseCase;
  private final GetHouseholdByIdUseCase getHouseholdByIdUseCase;

  private final UpdateHouseholdUseCase updateHouseholdUseCase;

  private static void checkHouseholdId(HouseholdDto householdDto, long householdId) {
    if (!Objects.equals(householdId, householdDto.id())) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,
          "Ambiguous resource id: found %s in body and %s in uri"
              .formatted(householdDto.id(), householdId));
    }
  }

  @PostMapping
  ResponseEntity<HouseholdDto> createHousehold(@RequestBody NewHouseholdDto newHouseholdDto) {
    var newHousehold = householdDtoMapper.map(newHouseholdDto);
    var createdHousehold =
        transactionalRunner.execute(() -> createHouseholdUseCase.execute(newHousehold));
    var responseDto = householdDtoMapper.map(createdHousehold);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
  }

  @GetMapping("/{householdId}")
  ResponseEntity<HouseholdDto> getHousehold(@PathVariable("householdId") long householdId) {
    var maybeDto =
        transactionalRunner
            .execute(() -> getHouseholdByIdUseCase.execute(householdId))
            .map(householdDtoMapper::map);
    return maybeDto
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.status(NOT_FOUND).build());
  }

  @PutMapping("/{householdId}")
  ResponseEntity<HouseholdDto> updateHousehold(
      @PathVariable("householdId") long householdId, @RequestBody HouseholdDto householdDto) {
    checkHouseholdId(householdDto, householdId);

    var household = householdDtoMapper.map(householdDto);
    var updated = transactionalRunner.execute(() -> updateHouseholdUseCase.execute(household));
    return ResponseEntity.ok(householdDtoMapper.map(updated));
  }
}
