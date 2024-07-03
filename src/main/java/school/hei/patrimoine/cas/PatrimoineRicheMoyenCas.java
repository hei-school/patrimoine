package school.hei.patrimoine.cas;

import school.hei.patrimoine.modele.Monnaie;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.time.LocalDate.now;
import static java.time.Month.*;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.util.stream.Collectors.toSet;

public class PatrimoineRicheMoyenCas implements Supplier<Patrimoine> {

  private final LocalDate ajd = now();
  private final LocalDate dans1mois = ajd.plusMonths(1);
  private final LocalDate finSimulation = LocalDate.of(2028, MARCH, 31);
  private final Monnaie arriary = new Monnaie("arriary", 4_821, ajd, -.1);

  private Set<Possession> possessionsCresus(Argent bp, Argent a2, Dette dette, Creance creance) {
    new FluxArgent("Loyer", bp, dans1mois, finSimulation, -1600, 25, arriary);
    new FluxArgent("Consommables", bp, dans1mois, finSimulation, -1000, 1, arriary);
    //note(verif)
    new FluxArgent("Contribution Cesar", bp, LocalDate.of(2024, AUGUST, 30), LocalDate.of(2025, JULY, 30), 2000, 25, arriary);

    var finSalaire = LocalDate.of(2024, SEPTEMBER, 30);
    new FluxArgent("Salaire 1", bp, ajd, finSalaire, 2100, 7, arriary);
    new FluxArgent("Salaire 2", bp, finSalaire.plusMonths(1), LocalDate.of(2025, DECEMBER, 31), 3_200/*note(verif)*/, 7, arriary);

    new FluxArgent("Prêt BP", bp, dans1mois, LocalDate.of(2027, APRIL, 30), -702, 7, arriary);
    new FluxArgent("O2", bp, ajd, finSalaire, -145, 20, arriary);

    var dateRembPva = LocalDate.of(2024, JULY, 14); //note(verif)
    new FluxArgent("Raliz", bp, dateRembPva, dateRembPva, -7_000, dateRembPva.getDayOfMonth(), arriary);
    new FluxArgent("Néri", bp, dateRembPva, dateRembPva, -5_000, dateRembPva.getDayOfMonth(), arriary);
    new FluxArgent("Hita", bp, dateRembPva, dateRembPva, -1_000, dateRembPva.getDayOfMonth(), arriary);

    var debutRembMyriade = LocalDate.of(2025, JANUARY, 30);
    new FluxArgent("Raly remb. Myriade", a2, debutRembMyriade, debutRembMyriade.plusYears(3), 500, 25, arriary);
    new FluxArgent("Rom remb. Myriade 1/2", a2, debutRembMyriade, debutRembMyriade.plusYears(1), 1050, 25, arriary);
    new FluxArgent("Rom remb. Myriade 2/2", a2, debutRembMyriade.plusYears(1), debutRembMyriade.plusYears(2), 969, 25, arriary);

    new FluxArgent("Graff & Pat", bp, ajd, LocalDate.of(2024, AUGUST, 30), -1400, 27, arriary);
    new FluxArgent("Graff & Pat remb.", a2, LocalDate.of(2024, SEPTEMBER, 30), LocalDate.of(2025, AUGUST, 30), 1680, 7, arriary);
    new FluxArgent("Mim remb.", a2, ajd, LocalDate.of(2026, MAY, 14), 500, 14, arriary);
    new FluxArgent("Fano créance", creance, ajd, ajd, 500 * (int) LocalDate.of(2023, SEPTEMBER, 1).until(ajd, MONTHS), ajd.getDayOfMonth(), arriary);
    new FluxArgent("Fano remb.", a2, ajd, LocalDate.of(2024, JULY, 27), 500, 27, arriary);

    var tauxAppreciationByzance = 0.1;
    var dateEmpruntZanzEtCie = LocalDate.of(2024, JULY, 5);
    var dateRembZanzEtCie = dateEmpruntZanzEtCie.plusYears(1);
    var dateRembPvaByzance = LocalDate.of(2025, JANUARY, 27);
    var byzance = new GroupePossession(
        "Byzance",
        LocalDate.of(2024, JANUARY, 1),
        Set.of(
            new Materiel("Byzance 1/3", ajd, 60_000, LocalDate.of(2024, JANUARY, 1), tauxAppreciationByzance, arriary),

            new FluxArgent("Zanz&Cie prêt", bp, dateEmpruntZanzEtCie, dateEmpruntZanzEtCie, 30_000, dateEmpruntZanzEtCie.getDayOfMonth(), arriary),
            new FluxArgent("Zanz&Cie dette creation", dette, dateEmpruntZanzEtCie, dateEmpruntZanzEtCie, -33_000, dateEmpruntZanzEtCie.getDayOfMonth(), arriary),
            new FluxArgent("Zanz&Cie remb.", bp, dateRembZanzEtCie, dateRembZanzEtCie, -33_000, dateRembZanzEtCie.getDayOfMonth(), arriary),
            new FluxArgent("Zanz&Cie dette annulation", dette, dateRembZanzEtCie, dateRembZanzEtCie, 33_000, dateRembZanzEtCie.getDayOfMonth(), arriary),

            new FluxArgent("Honoraires Villey Byzance", dette, ajd, ajd, -15_000, ajd.getDayOfMonth(), arriary),

            new FluxArgent("PVA&Cie Byzance remb.", bp, dateRembPvaByzance, dateRembPvaByzance, -60_000, dateRembPvaByzance.getDayOfMonth(), arriary),
            new FluxArgent("PVA&Cie Byzance Dette creation", dette, ajd, ajd, -60_000, ajd.getDayOfMonth(), arriary),
            new FluxArgent("PVA&Cie Byzance Dette annulation", dette, dateRembPvaByzance, dateRembPvaByzance, 60_000, dateRembPvaByzance.getDayOfMonth(), arriary),

            new AchatMaterielAuComptant("Byzance 2/3", LocalDate.of(2024, JULY, 31), 30_000, tauxAppreciationByzance, bp, arriary),
            new AchatMaterielAuComptant("Byzance 3/3", LocalDate.of(2025, JULY, 31), 30_000, tauxAppreciationByzance, bp, arriary)), arriary);
    new FluxArgent("Achat Byzance 1/2", bp, ajd, LocalDate.of(2024, AUGUST, 30), -1400, 27, arriary);

    return Set.of(
        bp,
        a2,
        dette,
        creance,
        byzance);
  }

  private Set<Possession> possessionsMyriade(Argent myriadeFr, Creance creanceFr, Dette detteMyriadeFr, Argent myriadeMg, Argent bp) {
    //note(verif)
    var dateRembCAR = LocalDate.of(2024, JULY, 10);
    new FluxArgent("CAR remb.", myriadeFr, dateRembCAR, dateRembCAR, 78_000, dateRembCAR.getDayOfMonth(), arriary);
    var dateRembCCA = LocalDate.of(2024, JULY, 15);
    new TransfertArgent("CCA remb.", myriadeFr, bp, dateRembCCA, dateRembCCA, 30_000, dateRembCCA.getDayOfMonth(), arriary);
    new FluxArgent("CAR 2024 créance", creanceFr, ajd, ajd, 45_000, ajd.getDayOfMonth(), arriary);
    var dateRembCAR24 = LocalDate.of(2025, JULY, 10);
    new TransfertArgent("CAR 2024 remb", creanceFr, myriadeFr, dateRembCAR24, dateRembCAR24, 45_000, dateRembCAR24.getDayOfMonth(), arriary);

    new FluxArgent("Cesar dette", detteMyriadeFr, ajd, ajd, -3_700 * (int) LocalDate.of(2024, MARCH, 1).until(ajd, MONTHS), ajd.getDayOfMonth(), arriary);
    new FluxArgent("Cesar salaire", myriadeFr, ajd, LocalDate.of(2024, DECEMBER, 27), -3_700, 27, arriary);
    new FluxArgent("SAFIR", myriadeFr, ajd, LocalDate.of(2024, DECEMBER, 27), -2_800, 16, arriary);
    new FluxArgent("Manuhis", myriadeFr, ajd, LocalDate.of(2024, DECEMBER, 27), -1_400, 26, arriary);
    new FluxArgent("Fitpal", myriadeFr, ajd, LocalDate.of(2024, DECEMBER, 27), -200, 20, arriary);

    var date1AvanceStratosphery = LocalDate.of(2024, JULY, 5);
    new FluxArgent("Avance Stratosphery 6 mois 1/2", myriadeFr, date1AvanceStratosphery, date1AvanceStratosphery, (int) (12_500 * (1 - 0.05)), date1AvanceStratosphery.getDayOfMonth(), arriary);
    var date2AvanceStratosphery = LocalDate.of(2024, AUGUST, 5);
    new FluxArgent("Avance Stratosphery 6 mois 2/2", myriadeFr, date2AvanceStratosphery, date2AvanceStratosphery, (int) (12_500 * (1 - 0.05)), date2AvanceStratosphery.getDayOfMonth(), arriary);

    var datePrestationVilley = LocalDate.of(2024, JULY, 30);
    new FluxArgent("Prestation Villey", myriadeMg, datePrestationVilley, datePrestationVilley, -1_000, datePrestationVilley.getDayOfMonth(), arriary);

    var dateRembITM = LocalDate.of(2025, JANUARY, 30);
    new FluxArgent("Prêt ITM remb.", myriadeMg, dateRembITM, dateRembITM, -11_000, dateRembITM.getDayOfMonth(), arriary);

    var finDontBeFoe = LocalDate.of(2025, DECEMBER, 5);
    new FluxArgent("DontBeFoe", myriadeFr, ajd, finDontBeFoe, 7_500, 3, arriary);
    new TransfertArgent("Myriade Fr --> Mg", myriadeFr, myriadeMg, ajd, finDontBeFoe, 5_000, 27, arriary);
    new FluxArgent("Charges Myriade Mg", myriadeMg, ajd, finDontBeFoe, -5_000, 27, arriary);

    return Set.of(myriadeFr, creanceFr, detteMyriadeFr, myriadeMg);
  }

  @Override
  public Patrimoine get() {
    var cresus = new Personne("Cresus");
    var bp = new Argent("BP Cresus & Cesar", ajd, 4_030, arriary);
    var a2 = new Argent("A2", ajd, 500, arriary);
    var creanceCresus = new Creance("Creance Cresus", ajd, 0, arriary);
    var detteCresus = new Dette("Dette Cresus", ajd, 0, arriary);
    Set<Possession> possessionsCresus = possessionsCresus(bp, a2, detteCresus, creanceCresus);

    var myriadeFr = new Argent("Myriade Fr", ajd, 840, arriary);
    var creanceMyriadeFr = new Creance("Creance Myriade Fr", ajd, 0, arriary);
    var detteMyriadeFr = new Dette("Dette Myriade Fr", ajd, 0, arriary);
    var myriadeMg = new Argent("Myriade Mg", ajd, 0, arriary);

    return
        new Patrimoine(
            "Cresus (moyen)",
            cresus,
            ajd,
            Stream
                .concat(
                    possessionsCresus.stream(),
                    possessionsMyriade(myriadeFr, creanceMyriadeFr, detteMyriadeFr, myriadeMg, bp).stream())
                .collect(toSet()));
  }
}
