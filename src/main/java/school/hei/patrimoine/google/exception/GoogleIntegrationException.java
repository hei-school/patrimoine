package school.hei.patrimoine.google.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GoogleIntegrationException extends Exception {
  public GoogleIntegrationException(String message, Exception cause) {
    super(message, cause);

    log.error(message, cause.getMessage());
  }
}
