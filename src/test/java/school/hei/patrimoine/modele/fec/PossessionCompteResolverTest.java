package school.hei.patrimoine.modele.fec;

import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.*;
import static school.hei.patrimoine.modele.fec.PossessionCompteResolver.resolve;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.FluxArgent;

class PossessionCompteResolverTest {

  @Test
  void should_return_comptes() {
    var compte = new Compte("Compte principal", now(), Argent.ariary(0));
    var possession = new FluxArgent("Achats", compte, now(), now(), 1, Argent.ariary(4000));
    var operation = OperationComptable.make(possession);

    var actual = resolve(operation);

    assertInstanceOf(PossessionCompteResolver.Comptes.class, actual);
  }
}
