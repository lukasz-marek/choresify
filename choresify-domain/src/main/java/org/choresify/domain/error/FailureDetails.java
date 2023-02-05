package org.choresify.domain.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@AllArgsConstructor(staticName = "of")
public class FailureDetails {

  Category category;
  String message;
}
