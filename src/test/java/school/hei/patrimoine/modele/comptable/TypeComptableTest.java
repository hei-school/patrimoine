package school.hei.patrimoine.modele.comptable;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.comptable.TypeComptable.*;

import org.junit.jupiter.api.Test;

class TypeComptableTest {
  @Test
  void test_enum_values() {
    assertEquals(5, TypeComptable.values().length);
    assertNotNull(TypeComptable.valueOf("PRODUIT"));
    assertNotNull(TypeComptable.valueOf("CHARGE"));
    assertNotNull(TypeComptable.valueOf("IMMOBILISATION"));
    assertNotNull(TypeComptable.valueOf("CCA"));
    assertNotNull(TypeComptable.valueOf("AUTRE"));
  }

  @Test
  void test_type_fec_enum_complet() {
    TypeComptable[] values = TypeComptable.values();
    assertEquals(5, values.length);

    for (TypeComptable type : values) {
      assertEquals(type, TypeComptable.valueOf(type.name()));
      assertNotNull(type.name());
      assertNotNull(type.toString());
      assertTrue(type.ordinal() < 5);
    }

    assertEquals(CCA, TypeComptable.values()[0]);
    assertEquals(PRODUIT, TypeComptable.values()[1]);
    assertEquals(IMMOBILISATION, TypeComptable.values()[2]);
    assertEquals(CHARGE, TypeComptable.values()[3]);
    assertEquals(AUTRE, TypeComptable.values()[4]);
  }
}
