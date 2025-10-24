package school.hei.patrimoine.patrilang.generator;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.PersonneMorale;

class VariableTypePatriLangGeneratorTest {
  private static final VariableTypePatriLangGenerator subject =
      new VariableTypePatriLangGenerator();
  private static final LocalDate date = LocalDate.of(2025, 1, 1);
  private static final Argent argent = ariary(1000);

  @Test
  void apply_creance_type() {
    var creance = new Creance("créances", date, argent);
    var actual = subject.apply(creance);

    assertEquals("Créances", actual);
  }

  @Test
  void apply_dette_type() {
    var dette = new Dette("dette", date, argent.mult(-1));
    var actual = subject.apply(dette);

    assertEquals("Dettes", actual);
  }

  @Test
  void apply_personne_type() {
    var personne = new Personne("Koto");
    var actual = subject.apply(personne);

    assertEquals("Personnes", actual);
  }

  @Test
  void apply_compte_type() {
    var compte = new Compte("comptePersonnel", date, argent);
    var actual = subject.apply(compte);

    assertEquals("Trésoreries", actual);
  }

  @Test
  void apply_personne_moral_type() {
    var personneMoral = new PersonneMorale("Strat-up");
    var actual = subject.apply(personneMoral);

    assertEquals("PersonnesMorales", actual);
  }
}
