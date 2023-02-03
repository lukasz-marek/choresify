package org.choresify.domain.common.validation;

import io.vavr.control.Validation;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public final class Validations {
  private Validations() {}

  @SafeVarargs
  public static <T, E> Validation<List<E>, T> joining(
      T validated, Function<T, Validation<E, ?>>... validators) {
    var validationErrors =
        Arrays.stream(validators)
            .map(validator -> validator.apply(validated))
            .filter(Validation::isInvalid)
            .map(Validation::getError)
            .toList();

    if (validationErrors.isEmpty()) {
      return Validation.valid(validated);
    }
    return Validation.invalid(validationErrors);
  }
}
