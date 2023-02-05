package org.choresify.domain.error;

import lombok.Value;

@Value(staticConstructor = "of")
public class FailureDetails {

  Category category;
  String message;
}
