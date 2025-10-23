package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.PersonneMorale;

class VariableTypePatriLangGeneratorTest {
  private final VariableTypePatriLangGenerator subject = new VariableTypePatriLangGenerator();
  private static final LocalDate date = LocalDate.of(2025, 1, 1);
  private final Argent argent = Argent.ariary(1000);

  @Test
  void apply_creance_type() {
    var creance = new Creance("créances", date, argent);
    var result = subject.apply(creance);

    assertEquals("Créances", result);
  }

  @Test
  void apply_dette_type() {
    var dette = new Dette("dette", date, argent.mult(-1));
    var result = subject.apply(dette);

    assertEquals("Dettes", result);
  }

  @Test
  void apply_personne_type() {
    var personne = new Personne("Koto");
    var result = subject.apply(personne);

    assertEquals("Personnes", result);
  }

  @Test
  void apply_compte_type() {
    var compte = new Compte("comptePersonnel", date, argent);
    var result = subject.apply(compte);

    assertEquals("Trésoreries", result);
  }

  @Test
  void apply_personne_moral_type() {
    var personneMoral = new PersonneMorale("Strat-up");
    var result = subject.apply(personneMoral);

    assertEquals("PersonnesMorales", result);
  }
}
