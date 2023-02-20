package org.choresify.domain.household.usecase;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.choresify.domain.household.model.Household;
import org.choresify.domain.household.port.Households;

@RequiredArgsConstructor
@Slf4j
public class DefaultGetHouseholdByIdUseCase implements GetHouseholdByIdUseCase {
  @SuppressFBWarnings("EI_EXPOSE_REP2")
  private final Households households;

  @Override
  public Optional<Household> execute(long id) {
    return Optional.empty();
  }
}
