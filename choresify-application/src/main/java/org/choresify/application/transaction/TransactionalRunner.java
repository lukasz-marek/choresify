package org.choresify.application.transaction;

import io.vavr.control.Either;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalRunner {
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public <E, T> Either<E, T> execute(Supplier<Either<E, T>> action) {
    return action.get();
  }
}
