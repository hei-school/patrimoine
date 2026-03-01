package school.hei.patrimoine.modele.fec.mapper;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.Argent.ariary;
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
  void fields_correctly_mapped() {
    var flux = new Argent(50, EUR);
    var compte = new Compte("Compte principal", now(), ariary(0));
    var ligne = LigneEcriture.builder().compte(compte).flux(flux).type(CHARGE).build();

    var actual = FECLineMapper.toFECLine(journal, ecriture, ligne).toArray();

    assertEquals("JN", actual[0]);
    assertEquals("Journal", actual[1]);
    assertEquals("JN001", actual[2]);
    assertEquals("20250115", actual[3]);
    assertEquals("CHARGE", actual[4]);
    assertEquals("Compte principal", actual[5]);
    assertEquals("", actual[6]);
    assertEquals("", actual[7]);
    assertEquals("", actual[8]);
    assertEquals("", actual[9]);
    assertEquals("Vente marchandises", actual[10]);
    assertEquals("50.00", actual[11]);
    assertEquals("", actual[12]);
    assertEquals("", actual[13]);
    assertEquals("", actual[14]);
    assertEquals("20250116", actual[15]);
    assertEquals("253050.00", actual[16]);
    assertEquals("MGA", actual[17]);
  }
}
