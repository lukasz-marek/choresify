package org.choresify.application.transaction;

import io.vavr.control.Validation;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class TransactionalRunner {
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public <E, T> Validation<E, T> execute(Supplier<Validation<E, T>> action) {
    return action.get();
  }
}
