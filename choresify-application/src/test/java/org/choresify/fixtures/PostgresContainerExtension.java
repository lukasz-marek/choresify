package org.choresify.fixtures;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.PostgreSQLContainer;

class PostgresContainerExtension implements BeforeAllCallback, AfterAllCallback {
  private static final Logger log = LoggerFactory.getLogger(PostgresContainerExtension.class);
  private final PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE);

  @Override
  public void beforeAll(ExtensionContext context) {
    log.info("Starting PostgreSQL container");
    container.start();
    log.info("Successfully started PostgreSQL container");
    setApplicationProperties();
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
  public void afterAll(ExtensionContext context) {
    log.info("Ensuring PostgreSQL container is stopped");
    if (container.isRunning()) {
      container.stop();
    }
    log.info("PostgreSQL container is now stopped");
  }
}
