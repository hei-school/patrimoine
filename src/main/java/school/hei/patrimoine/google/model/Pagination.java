package school.hei.patrimoine.google.model;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Builder(toBuilder = true)
public class Pagination {
  private final Integer pageSize;
  private final String nextToken;
  private final String prevToken;
  private final String currentToken;

  public String createCacheKey(String fileId) {
    return String.format("%s_%s", this, fileId);
  }

  public boolean hasNext() {
    return nextToken != null;
  }

  public boolean hasPrev() {
    return prevToken != null;
  }

  private String getNextKey() {
    return nextToken == null ? "lastPage" : nextToken;
  }

  private String getPrevKey() {
    return prevToken == null ? "firstPage" : prevToken;
  }

  private String getCurrentKey() {
    return currentToken == null ? "lastPage" : currentToken;
  }

  @Override
  public String toString() {
    return "pageSize="
        + pageSize
        + "prevToken="
        + getPrevKey()
        + "currentToken="
        + getCurrentKey()
        + "nextToken="
        + getNextToken();
  }

  public Pagination next() {
    return this.toBuilder().nextToken(null).prevToken(currentToken).currentToken(nextToken).build();
  }

  public Pagination prev() {
    return this.toBuilder().prevToken(null).nextToken(currentToken).currentToken(prevToken).build();
  }
}
