package school.hei.patrimoine.modele.fec;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

public class FECLineTest {
  @Test
  void toArray_retourne_les_valeurs_en_tableau() {
    var line = new FECLine(List.of("JN", "Journal", "JN001"));
    var array = line.toArray();

    assertArrayEquals(new String[] {"JN", "Journal", "JN001"}, array);
  }

  @Test
  void toArray_retourne_tableau_vide_si_aucune_valeur() {
    var line = new FECLine(List.of());
    assertEquals(0, line.toArray().length);
  }

  @Test
  void header_retourne_les_labels_des_colonnes_FEC() {
    var header = FECLine.header();

    assertNotNull(header);
    assertTrue(header.length > 0);
    assertEquals(FECColumn.values().length, header.length);
  }

  @Test
  void header_contient_les_bons_labels() {
    var header = FECLine.header();
    var columns = FECColumn.values();

    for (int i = 0; i < columns.length; i++) {
      assertEquals(columns[i].label(), header[i]);
    }
  }
}
