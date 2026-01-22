package school.hei.patrimoine.modele.possession;

import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.possession.TypeFEC.*;

import org.junit.jupiter.api.Test;

class TypeFECTest {
  @Test
  void test_enum_values() {
    assertEquals(5, TypeFEC.values().length);
    assertNotNull(TypeFEC.valueOf("PRODUIT"));
    assertNotNull(TypeFEC.valueOf("CHARGE"));
    assertNotNull(TypeFEC.valueOf("IMMOBILISATION"));
    assertNotNull(TypeFEC.valueOf("CCA"));
    assertNotNull(TypeFEC.valueOf("AUTRE"));
  }

  @Test
  void test_type_fec_enum_complet() {
    TypeFEC[] values = TypeFEC.values();
    assertEquals(5, values.length);

    for (TypeFEC type : values) {
      assertEquals(type, TypeFEC.valueOf(type.name()));
      assertNotNull(type.name());
      assertNotNull(type.toString());
      assertTrue(type.ordinal() < 5);
    }

    assertEquals(CCA, TypeFEC.values()[0]);
    assertEquals(PRODUIT, TypeFEC.values()[1]);
    assertEquals(IMMOBILISATION, TypeFEC.values()[2]);
    assertEquals(CHARGE, TypeFEC.values()[3]);
    assertEquals(AUTRE, TypeFEC.values()[4]);
  }
}
