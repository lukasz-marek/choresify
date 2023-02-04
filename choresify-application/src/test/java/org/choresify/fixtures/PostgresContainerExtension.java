package org.choresify.fixtures;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

class PostgresContainerExtension implements BeforeAllCallback, CloseableResource {
  private static final Logger log = LoggerFactory.getLogger(PostgresContainerExtension.class);
  private static final String CONTEXT_KEY = "postgresql_container_initialized";
  private final PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE);

  @Override
  public synchronized void beforeAll(ExtensionContext context) {
    if (!containerAlreadyCreated(context)) {
      log.info("Starting PostgreSQL container");
      container.start();
      log.info("Successfully started PostgreSQL container");
      setApplicationProperties();
      markContainerAsCreated(context);
    }
  }

  private boolean containerAlreadyCreated(ExtensionContext context) {
    return context.getRoot().getStore(Namespace.GLOBAL).get(CONTEXT_KEY) != null;
  }

  private void markContainerAsCreated(ExtensionContext context) {
    context.getRoot().getStore(Namespace.GLOBAL).put(CONTEXT_KEY, this);
  }

  private void setApplicationProperties() {
    log.info("Initializing Spring DataSource properties");
    System.setProperty("spring.datasource.url", container.getJdbcUrl());
    System.setProperty("spring.datasource.username", container.getUsername());
    System.setProperty("spring.datasource.password", container.getPassword());
    System.setProperty("spring.datasource.driver-class-name", container.getDriverClassName());
    log.info("Initialized Spring DataSource properties");
  }

  @Override
  public void close() {
    log.info("Ensuring PostgreSQL container is stopped");
    if (container.isRunning()) {
      container.stop();
    }
    log.info("PostgreSQL container is now stopped");
  }
}
