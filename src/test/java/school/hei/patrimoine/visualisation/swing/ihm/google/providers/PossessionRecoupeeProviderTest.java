package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.recouppement.model.CompteGetter.getComptes;
import static school.hei.patrimoine.modele.recouppement.model.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.model.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.PossessionRecoupeeProvider.*;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

class PossessionRecoupeeProviderTest {
  private static final LocalDate DEBUT = LocalDate.of(2025, JANUARY, 1);
  private static final LocalDate FIN = LocalDate.of(2025, DECEMBER, 31);

  @Test
  void getList_empty_data() {
    var cas = cas(Set.of());

    var subject = subject(cas);
    var actual = subject.getList(meta(cas, cas), filter("", pagination(), Set.of()));

    assertTrue(actual.data().isEmpty());
  }

  @Test
  void getList_with_status_filter() {
    var compte = new Compte("compte1", DEBUT, ariary(100));

    var cas = cas(Set.of(compte));
    var subject = subject(cas);

    var actual =
        subject.getList(meta(cas, cas), filter("", pagination(), Set.of(EXECUTE_SANS_CORRECTION)));

    assertEquals(1, actual.data().size());
    assertTrue(
        actual.data().stream()
            .allMatch(recoupee -> EXECUTE_SANS_CORRECTION.equals(recoupee.status())));
  }

  @Test
  void getList_with_name_filter() {
    var compteSpecial = new Compte("compteSpecial", DEBUT, ariary(100));
    var compteBOA = new Compte("compteBOA", DEBUT, ariary(100));

    var cas = cas(Set.of(compteSpecial, compteBOA));

    var subject = subject(cas);
    var result = subject.getList(meta(cas, cas), filter("special", pagination(), Set.of()));

    assertEquals(1, result.data().size());
    assertTrue(result.data().getFirst().possession().nom().contains("Special"));
  }

  @Test
  void getList_out_of_bounds_pagination() {
    var compte = new Compte("compte1", DEBUT, ariary(100));
    var cas = cas(Set.of(compte));

    var subject = subject(cas);
    var actual = subject.getList(meta(cas, cas), filter("", new Pagination(2, 10), Set.of()));

    assertTrue(actual.data().isEmpty());
  }

  @Test
  void getList_pagination_total_pages() {
    var compte1 = new Compte("compte1", DEBUT, ariary(100));
    var compte2 = new Compte("compte2", DEBUT, ariary(200));
    var cas = cas(Set.of(compte1, compte2));

    var subject = subject(cas);

    var actual = subject.getList(meta(cas, cas), filter("", new Pagination(1, 1), Set.of()));

    assertEquals(1, actual.data().size());
    assertEquals(2, actual.totalPage());
  }

  private static PossessionRecoupeeProvider subject(Cas doneCas) {
    return new PossessionRecoupeeProvider(getComptes(doneCas));
  }

  private static Meta meta(Cas planned, Cas done) {
    return Meta.builder().planned(planned).done(done).build();
  }

  private static Pagination pagination() {
    return new Pagination(1, 10);
  }

  private static Filter filter(String nom, Pagination pagination, Set<RecoupementStatus> statuses) {
    return Filter.builder()
        .debut(DEBUT)
        .fin(FIN)
        .nom(nom)
        .pagination(pagination)
        .statuses(statuses)
        .build();
  }

  private static Cas cas(Set<Possession> possessions) {
    return new Cas(DEBUT, FIN, Map.of()) {
      @Override
      protected Devise devise() {
        return null;
      }

      @Override
      protected String nom() {
        return "patrimoine";
      }

      @Override
      protected void init() {}

      @Override
      protected void suivi() {}

      @Override
      public Set<Possession> possessions() {
        return possessions;
      }
    };
  }
}
