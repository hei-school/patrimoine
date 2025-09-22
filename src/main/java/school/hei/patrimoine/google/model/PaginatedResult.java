package school.hei.patrimoine.google.model;

public record PaginatedResult<T>(T data, Pagination nextPagination) {
  public Pagination getNextPagination() {
    return nextPagination;
  }

  public static <T> PaginatedResult<T> of(T data, Pagination nextPagination) {
    return new PaginatedResult<>(data, nextPagination);
  }
}
