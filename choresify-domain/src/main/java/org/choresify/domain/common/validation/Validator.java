package org.choresify.domain.common.validation;

@FunctionalInterface
public interface Validator<T> {
  void validate(T entity);
}
