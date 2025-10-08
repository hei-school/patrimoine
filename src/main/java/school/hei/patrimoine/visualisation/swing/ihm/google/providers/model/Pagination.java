package school.hei.patrimoine.visualisation.swing.ihm.google.providers.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record Pagination(int page, int size) {}
