package school.hei.patrimoine.google.cache;

import school.hei.patrimoine.google.exception.GoogleIntegrationException;

@FunctionalInterface
public interface ThrowingSupplier<R> {
  R get() throws GoogleIntegrationException;
}
