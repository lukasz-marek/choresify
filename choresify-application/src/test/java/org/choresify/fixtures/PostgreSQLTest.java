package org.choresify.fixtures;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(PostgresContainerExtension.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostgreSQLTest {}
