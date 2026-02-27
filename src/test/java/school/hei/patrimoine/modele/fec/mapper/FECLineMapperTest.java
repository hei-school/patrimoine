package school.hei.patrimoine.modele.fec.mapper;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Devise.EUR;
import static school.hei.patrimoine.modele.comptable.TypeComptable.CHARGE;
import static school.hei.patrimoine.modele.fec.JournalCode.JN;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.fec.EcritureComptable;
import school.hei.patrimoine.modele.fec.Journal;
import school.hei.patrimoine.modele.fec.LigneEcriture;
import school.hei.patrimoine.modele.possession.Compte;

class FECLineMapperTest {
  private Journal journal;
  private EcritureComptable ecriture;

  @BeforeEach
  void setUp() {
    journal = new Journal(JN, "Journal");
    ecriture =
        EcritureComptable.builder()
            .id("JN001")
            .date(LocalDate.of(2025, 1, 15))
            .libelle("Vente marchandises")
            .lignes(List.of())
            .dateValidation(LocalDate.of(2025, 1, 16))
            .build();
  }

  @Test
  void journal_and_ecriture_fields_correctly_mapped() {
    var flux = new Argent(50.00, EUR);
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var ligne = LigneEcriture.builder().compte(compte).flux(flux).type(CHARGE).build();

    var fecLine = FECLineMapper.toFECLine(journal, ecriture, ligne);

    assertEquals("JN", fecLine.journalCode());
    assertEquals("Journal", fecLine.journalLib());
    assertEquals("JN001", fecLine.ecritureNum());
    assertEquals("20250115", fecLine.ecritureDate());
    assertEquals("20250116", fecLine.validDate());
    assertEquals("Vente marchandises", fecLine.ecritureLib());
  }
}
