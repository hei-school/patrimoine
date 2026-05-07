package school.hei.patrimoine.modele.comptable.fec;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.comptable.fec.FECColumn.*;
import static school.hei.patrimoine.modele.comptable.fec.FECLine.headers;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

class FECLineTest {

  @Test
  void headers_should_contain_18_correct_column() {
    var headers = headers();

    assertEquals(18, headers.length);
    assertEquals("JournalCode", headers[0]);
    assertEquals("JournalLib", headers[1]);
    assertEquals("EcritureNum", headers[2]);
    assertEquals("EcritureDate", headers[3]);
    assertEquals("CompteNum", headers[4]);
    assertEquals("CompteLib", headers[5]);
    assertEquals("CompAuxNum", headers[6]);
    assertEquals("CompAuxLib", headers[7]);
    assertEquals("PieceRef", headers[8]);
    assertEquals("PieceDate", headers[9]);
    assertEquals("EcritureLib", headers[10]);
    assertEquals("Debit", headers[11]);
    assertEquals("Credit", headers[12]);
    assertEquals("EcritureLet", headers[13]);
    assertEquals("DateLet", headers[14]);
    assertEquals("ValidDate", headers[15]);
    assertEquals("Montantdevise", headers[16]);
    assertEquals("Idevise", headers[17]);
  }

  @Test
  void values_should_match() {
    var input = new HashMap<FECColumn, String>();
    input.put(JOURNAL_CODE, "JN");
    input.put(JOURNAL_LIB, "Journal");
    input.put(ECRITURE_NUM, "JN001");
    input.put(ECRITURE_LIB, "Achat d'ordinateur");

    var line = new FECLine(input);
    var values = line.values();

    assertEquals("JN", values[0]);
    assertEquals("Journal", values[1]);
    assertEquals("JN001", values[2]);
    assertEquals("Achat d'ordinateur", values[10]);
  }

  @Test
  void default_value_should_be_an_empty_string() {
    var input = new HashMap<FECColumn, String>();
    input.put(JOURNAL_CODE, "JN");
    input.put(ECRITURE_NUM, "JN001");

    var line = new FECLine(input);
    var values = line.values();

    assertEquals("JN", values[0]);
    assertEquals("", values[1]);
    assertEquals("JN001", values[2]);
    assertEquals("", values[3]);
  }
}
