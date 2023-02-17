package org.choresify.application.common.transaction;

import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalRunner {
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public <T> T execute(Supplier<T> action) {
    return action.get();
  }
}
