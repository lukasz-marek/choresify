package org.choresify.domain.common.validation;

import io.vavr.control.Validation;
import org.choresify.domain.error.Failure;

@FunctionalInterface
public interface Validator<T> {
  Validation<Failure, T> validate(T entity);
}
