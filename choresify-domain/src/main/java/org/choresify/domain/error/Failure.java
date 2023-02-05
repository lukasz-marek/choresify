package org.choresify.domain.error;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Singular;
import lombok.Value;

@Value(staticConstructor = "of")
public class Failure {
  @Singular Set<FailureDetails> failureDetails;

  public static Failure of(Collection<FailureDetails> details) {
    return new Failure(new HashSet<>(details));
  }

  public static Failure of(Category category, String message) {
    return Failure.of(Collections.singleton(FailureDetails.of(category, message)));
  }

  public static Failure of(Category category, Collection<String> messages) {
    var details =
        messages.stream().map(message -> FailureDetails.of(category, message)).collect(toSet());
    return Failure.of(details);
  }

  public Set<FailureDetails> getFailureDetails() {
    return Set.copyOf(failureDetails);
  }
}
