package school.hei.patrimoine.visualisation.web.components;

import school.hei.patrimoine.modele.evolution.FluxJournalier;

public class FluxJournalierGrid extends RoundedGrid<FluxJournalier> {
  public FluxJournalierGrid() {
    super(FluxJournalier.class);
    addColumn("Date", FluxJournalier::date);
    addColumn("Compte", FluxJournalier::compte);
    addColumn("Flux", FluxJournalier::flux);
  }
}
