package school.hei.patrimoine.modele.fec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import school.hei.patrimoine.modele.comptable.OperationComptable;
import school.hei.patrimoine.modele.comptable.TypeComptable;
import school.hei.patrimoine.modele.fec.factory.EcritureComptableFactory;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.pj.PieceJustificative;

public class Journal {
  private final JournalCode code;
  private final String libelle;
  private final List<EcritureComptable> ecritures = new ArrayList<>();
  private final Map<String, Integer> compteSequences = new HashMap<>();

  public Journal(JournalCode code, String libelle) {
    this.code = code;
    this.libelle = libelle;
  }

  public JournalCode code() {
    return code;
  }

  public String libelle() {
    return libelle;
  }

  public List<EcritureComptable> ecritures() {
    return ecritures;
  }

  public void addEcriture(OperationComptable operation, PieceJustificative pj) {
    var ecritureToAdd = EcritureComptableFactory.make(this, operation, pj);
    ecritures.add(ecritureToAdd);
  }

  public String getNextId() {
    return code().toString() + String.format("%03d", ecritures.size());
  }

  public String getNextCompteNum(Compte compte, TypeComptable type) {
    String baseCode = type.codePCG();
    String compteKey = checkingCompteNotNull(compte);
    int seq = compteSequences.computeIfAbsent(compteKey, k -> compteSequences.size());
    return baseCode + String.format("%03d", seq);
  }

  private String checkingCompteNotNull(Compte compte) {
    if (compte == null) {
      return "inconnu";
    }
    return compte.nom();
  }
}
