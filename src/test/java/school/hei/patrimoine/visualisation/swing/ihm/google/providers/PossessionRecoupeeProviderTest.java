package school.hei.patrimoine.visualisation.swing.ihm.google.providers;

import static java.time.Month.JANUARY;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Devise;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.RecoupementStatus;
import school.hei.patrimoine.visualisation.swing.ihm.google.providers.model.Pagination;

class PossessionRecoupeeProviderTest {

  @Test
  void getList_empty_data() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var provider = new PossessionRecoupeeProvider();
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
        provider.getList(
            PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
            PossessionRecoupeeProvider.Filter.builder()
                .statuses(Set.of())
                .pagination(new Pagination(1, 10))
                .filterName("")
                .build());

    assertNotNull(result);
    assertTrue(result.possessionRecoupees().isEmpty());
  }

  @Test
  void getList_with_status_filter() {
    var date = LocalDate.of(2025, JANUARY, 1);
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

    var provider = new PossessionRecoupeeProvider();
    var result =
        provider.getList(
            PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
            PossessionRecoupeeProvider.Filter.builder()
                .statuses(Set.of(RecoupementStatus.EXECUTE_SANS_CORRECTION))
                .pagination(new Pagination(1, 10))
                .filterName("")
                .build());
    assertNotNull(result);
    assertFalse(result.possessionRecoupees().isEmpty());
    assertTrue(
        result.possessionRecoupees().stream()
            .allMatch(p -> p.status() == RecoupementStatus.EXECUTE_SANS_CORRECTION));
  }

  @Test
  void getList_with_name_filter() {
    var date = LocalDate.of(2025, JANUARY, 1);
    var compte = new Compte("compteSpecial", date, Argent.ariary(100));

    var cas =
        new Cas(date, date, Map.of()) {
          @Override
          protected void init() {}

          @Override
          protected void suivi() {}

          @Override
          public Set<school.hei.patrimoine.modele.possession.Possession> possessions() {
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

    var provider = new PossessionRecoupeeProvider();
    var result =
        provider.getList(
            PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
            PossessionRecoupeeProvider.Filter.builder()
                .statuses(Set.of(RecoupementStatus.EXECUTE_SANS_CORRECTION))
                .pagination(new Pagination(1, 10))
                .filterName("special")
                .build());
    assertNotNull(result);
    assertEquals(1, result.possessionRecoupees().size());
    assertTrue(result.possessionRecoupees().get(0).possession().nom().contains("Special"));
  }

  @Test
  void getList_out_of_bounds_pagination() {
    var date = LocalDate.of(2025, JANUARY, 1);
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

    var provider = new PossessionRecoupeeProvider();
    var result =
        provider.getList(
            PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
            PossessionRecoupeeProvider.Filter.builder()
                .statuses(Set.of(RecoupementStatus.EXECUTE_SANS_CORRECTION))
                .pagination(new Pagination(2, 10))
                .filterName("")
                .build());
    assertNotNull(result);
    assertTrue(result.possessionRecoupees().isEmpty());
  }

  @Test
  void getList_pagination_total_pages() {
    var date = LocalDate.of(2025, JANUARY, 1);
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

    var provider = new PossessionRecoupeeProvider();
    var result =
        provider.getList(
            PossessionRecoupeeProvider.Meta.builder().planned(cas).done(cas).build(),
            PossessionRecoupeeProvider.Filter.builder()
                .statuses(Set.of(RecoupementStatus.EXECUTE_SANS_CORRECTION))
                .pagination(new Pagination(1, 1))
                .filterName("")
                .build());
    assertNotNull(result);
    assertEquals(1, result.possessionRecoupees().size());
    assertEquals(2, result.totalPage());
  }
}
