package school.hei.patrimoine.visualisation.swing.ihm.google.providers.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Pagination(int page, int size) {
  public Pagination(int page, int size) {
    this.page = Math.max(1, page);
    this.size = Math.max(1, size);
  }
}
