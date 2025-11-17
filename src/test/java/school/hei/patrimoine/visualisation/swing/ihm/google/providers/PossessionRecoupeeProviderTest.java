package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.recouppement.CompteGetterFactory.getComptes;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

class PossessionRecoupeeProviderTest {
  private static final LocalDate date = LocalDate.of(2025, JANUARY, 1);

  private static PossessionRecoupeeProvider subject(Cas doneCas) {
    return new PossessionRecoupeeProvider(getComptes(doneCas.patrimoine()));
  }

  @Test
  void getList_empty_data() {

    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected Devise devise() {
            return null;
          }

          @Override
          protected String nom() {
            return "";
          }

          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of();
          }
        };
    var result =
        subject(cas)
            .getList(
                PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
                PossessionRecoupeeProvider.Filter.builder()
                    .statuses(Set.of())
                    .pagination(new Pagination(1, 10))
                    .filterName("")
                    .build());

    assertTrue(result.possessionRecoupees().isEmpty());
  }

  @Test
  void getList_with_status_filter() {
    var compte = new Compte("compte1", date, Argent.ariary(100));

    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(compte);
          }

          @Override
          protected Devise devise() {
            return Devise.MGA;
          }

          @Override
          protected String nom() {
            return "patrimoine";
          }
        };

    var result =
        subject(cas)
            .getList(
                PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
                PossessionRecoupeeProvider.Filter.builder()
                    .statuses(Set.of(EXECUTE_SANS_CORRECTION))
                    .pagination(new Pagination(1, 10))
                    .filterName("")
                    .build());

    assertFalse(result.possessionRecoupees().isEmpty());
    assertTrue(
        result.possessionRecoupees().stream()
            .allMatch(p -> EXECUTE_SANS_CORRECTION.equals(p.status())));
  }

  @Test
  void getList_with_name_filter() {
    var compte = new Compte("compteSpecial", date, Argent.ariary(100));

    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(compte);
          }

          @Override
          protected Devise devise() {
            return Devise.MGA;
          }

          @Override
          protected String nom() {
            return "patrimoine";
          }
        };

    var result =
        subject(cas)
            .getList(
                PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
                PossessionRecoupeeProvider.Filter.builder()
                    .statuses(Set.of(EXECUTE_SANS_CORRECTION))
                    .pagination(new Pagination(1, 10))
                    .filterName("special")
                    .build());

    assertEquals(1, result.possessionRecoupees().size());
    assertTrue(result.possessionRecoupees().getFirst().possession().nom().contains("Special"));
  }

  @Test
  void getList_out_of_bounds_pagination() {
    var compte = new Compte("compte1", date, Argent.ariary(100));
    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(compte);
          }

          @Override
          protected Devise devise() {
            return Devise.MGA;
          }

          @Override
          protected String nom() {
            return "patrimoine";
          }
        };

    var result =
        subject(cas)
            .getList(
                PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
                PossessionRecoupeeProvider.Filter.builder()
                    .statuses(Set.of(EXECUTE_SANS_CORRECTION))
                    .pagination(new Pagination(2, 10))
                    .filterName("")
                    .build());

    assertTrue(result.possessionRecoupees().isEmpty());
  }

  @Test
  void getList_pagination_total_pages() {
    var compte1 = new Compte("compte1", date, Argent.ariary(100));
    var compte2 = new Compte("compte2", date, Argent.ariary(200));
    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<Possession> possessions() {
            return Set.of(compte1, compte2);
          }

          @Override
          protected Devise devise() {
            return Devise.MGA;
          }

          @Override
          protected String nom() {
            return "patrimoine";
          }
        };

    var result =
        subject(cas)
            .getList(
                PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
                PossessionRecoupeeProvider.Filter.builder()
                    .statuses(Set.of(EXECUTE_SANS_CORRECTION))
                    .pagination(new Pagination(1, 1))
                    .filterName("")
                    .build());

    assertEquals(1, result.possessionRecoupees().size());
    assertEquals(2, result.totalPage());
  }
}
