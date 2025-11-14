package school.hei.patrimoine.modele.recouppement.generateur;

import static java.lang.Double.parseDouble;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Argent.ariary;
import static school.hei.patrimoine.modele.recouppement.RecoupementStatus.EXECUTE_SANS_CORRECTION;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.util.*;
import school.hei.patrimoine.modele.Argent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee;
import school.hei.patrimoine.modele.recouppement.PossessionRecoupee.Info;

public class RecoupeurDePossessionBase<T extends Possession> implements RecoupeurDePossession<T> {
  protected String nommeAsImprevu(T possession, LocalDate t, Argent valeur) {
    return String.format(
        "Imprevu[date=%s, nom=%s, valeur=%s]", t, possession.nom(), format(valeur));
  }

  protected String nommerAsNonExecute(T possession, LocalDate t, Argent valeur) {
    return String.format(
        "NonExecuté[date=%s, nom=%s, valeur=%s]", t, possession.nom(), format(valeur));
  }

  protected String nommerAsEnRetard(T possession, LocalDate t, Argent valeur) {
    return String.format(
        "EnRetard[date=%s, nom=%s, valeur=%s]", t, possession.nom(), format(valeur));
  }

  protected String nommerAsEnAvance(T possession, LocalDate t, Argent valeur) {
    return String.format(
        "EnAvance[date=%s, nom=%s, valeur=%s]", t, possession.nom(), format(valeur));
  }

  protected String nommerAsValeurDifferentes(T possession, LocalDate t, Argent valeur) {
    return String.format(
        "ValeurDifferentes[date=%s, nom=%s, valeur=%s]", t, possession.nom(), format(valeur));
  }

  protected LocalDate getDate(T possession) {
    return possession.t();
  }

  protected Argent getValeur(T possession) {
    return possession.valeurComptable();
  }

  protected Argent getValeur(Collection<T> possessions) {
    var somme = ariary(0);
    for (var possession : possessions) {
      somme = getValeur(possession).add(somme, possession.t());
    }

    return somme;
  }

  protected List<T> sorted(Collection<T> possessions) {
    return possessions.stream().sorted(Comparator.comparing(this::getDate)).toList();
  }

  protected Info toInfo(T possession) {
    return new Info(getDate(possession), getValeur(possession), possession);
  }

  protected Set<Info> toInfo(Collection<T> possessions) {
    return possessions.stream().map(this::toInfo).collect(toSet());
  }

  // Valeur de correction = la somme des valeurs réalisées - Valeur prévue
  protected Argent getCorrectionValeur(T prevu, Set<T> realises) {
    if (prevu == null) {
      return getValeur(realises);
    }

    return getValeur(realises).minus(getValeur(prevu), prevu.t());
  }

  protected Map<LocalDate, Set<T>> groupByDate(Collection<T> possessions) {
    Map<LocalDate, Set<T>> grouped = new HashMap<>();

    for (T possession : possessions) {
      grouped.computeIfAbsent(possession.t(), t -> new HashSet<>()).add(possession);
    }

    return grouped;
  }

  @Override
  public PossessionRecoupee comparer(T prevu, Set<T> realises) {
    return PossessionRecoupee.builder()
        .prevu(Info.of(prevu))
        .realises(realises.stream().map(Info::of).collect(toSet()))
        .status(EXECUTE_SANS_CORRECTION)
        .corrections(Set.of())
        .build();
  }

  @Override
  public PossessionRecoupee nonExecute(T nonExecute, Set<Possession> possessions) {
    return PossessionRecoupee.builder()
        .prevu(Info.of(nonExecute))
        .realises(Set.of(Info.of(nonExecute)))
        .status(EXECUTE_SANS_CORRECTION)
        .corrections(Set.of())
        .build();
  }

  @Override
  public PossessionRecoupee imprevu(T nonPrevu) {
    return PossessionRecoupee.builder()
        .prevu(Info.of(nonPrevu))
        .realises(Set.of(Info.of(nonPrevu)))
        .status(EXECUTE_SANS_CORRECTION)
        .corrections(Set.of())
        .build();
  }

  private static String format(Argent argent) {
    var symbols = new DecimalFormatSymbols(Locale.FRANCE);
    symbols.setGroupingSeparator('_');
    symbols.setDecimalSeparator('.');

    var df = new DecimalFormat("#,##0.##", symbols);
    df.setGroupingUsed(true);

    return String.format("%s%s", df.format(parseDouble(argent.ppMontant())), argent.devise());
  }
}
