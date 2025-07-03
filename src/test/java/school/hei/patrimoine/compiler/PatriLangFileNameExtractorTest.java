package school.hei.patrimoine.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PatriLangFileNameExtractorTest {
  @Test
  void extract_cas_set_filename_from_string_ok() {
    String code =
        """
            # Général
            `/* Objectif final 4884000Ar */`
            * Objectif final (((4000000 + (-(-888000)) - 4000 * 2 + 4000) * 3) / 3) / 2 * 2 Ar
            \n
            # Cas
            * ZetyPersonnel
            * LocationMaison
            \n
            # Dates
            * Dates:ajd: le 10 du 01-2025
            * Dates:finSimulation: le 10 du 04-2025
            \n
            # Personnes
            * Zety
            * Lita
            * Rasoa
            # Trésoreries
            * loyerMaison, valant 0Ar Dates:ajd
            * zetyPersonnel, valant 0Ar Dates:ajd
            * zetyLoyerMaison, valant 0Ar Dates:ajd
            * litaPersonnel, valant 0Ar Dates:ajd
            * rasoaPersonnel, valant 0Ar Dates:ajd
            # Créances
            * zetyCreance, valant 0Ar Dates:ajd
            # Dettes
            * zetyDette, valant 0Ar Dates:ajd
        """;

    var subject = new PatriLangFileNameExtractor();

    assertEquals("CasSet.tout.md", subject.apply(code));
  }

  @Test
  void extract_cas_filename_from_string_ok() {
    String code =
        """
    # Général
    * Spécifier Dates:ajd
    * Fin de simulation Dates:finSimulation - 1 année et 2 mois et 10 jours `/* Retard sur les actions */`
    * Cas de LocationMaison
    * Devise en Ar

    # Possesseurs
    * Personnes:Zety ((40 * 2 / 2) - 2 + 3 * 1 - 1)%
    * Personnes:Lita 10%
    * Personnes:Rasoa 50%

    # Trésoreries
    * Trésoreries:loyerMaison

    # Initialisation
    * `objectifInitLocationMaison` Dates:ajd, objectif de 500000Ar pour Trésoreries:loyerMaison
    * `initCompteLoyerMaison` Dates:ajd, entrer 500000Ar vers Trésoreries:loyerMaison `/* Commentaire */`\s

    # Opérations
    ## Rem2025, Dates:ajd, devise en Ar
    * `remZety2025` `/* Commentaire */` Dates:ajd, entrer 400000Ar vers Trésoreries:zetyLoyerMaison , jusqu'à le 31 du 12-2025 tous les 1 du mois
    * `remRasoa2025` Dates:ajd, entrer 500000Ar vers Trésoreries:rasoaPersonnel , jusqu'à le 31 du 12-2025 tous les 1 du mois
    * `remLita2025` Dates:ajd, entrer 100000Ar vers Trésoreries:litaPersonnel , jusqu'à le 31 du 12-2025 tous les 1 du mois

    ## RevenusLoyer, Dates:ajd, devise en Ar
    * `receptionLoyer + Dates:ajd` Dates:ajd, entrer 1000000Ar vers Trésoreries:loyerMaison , jusqu'à date indéterminée tous les 29 du mois

    ## ChargesLoyer, Dates:ajd, devise en Ar
    * `paiementCommune + Dates:ajd` Dates:ajd, sortir 200000Ar depuis Trésoreries:loyerMaison, jusqu'à date indéterminée tous les 01 du mois
    * `JIRAMA + Dates:ajd` Dates:ajd, sortir 100000Ar depuis Trésoreries:loyerMaison, jusqu'à date indéterminée tous les 01 du mois
""";

    var subject = new PatriLangFileNameExtractor();

    assertEquals("LocationMaison.cas.md", subject.apply(code));
  }

  @Test
  void extract_filename_name_from_string_ko() {
    PatriLangFileNameExtractor subject = new PatriLangFileNameExtractor();
    String invalidCode = "int x = 42";

    Exception exception =
        assertThrows(IllegalArgumentException.class, () -> subject.apply(invalidCode));

    assertEquals("Wrong PatriLang content detected.", exception.getMessage());
  }
}
