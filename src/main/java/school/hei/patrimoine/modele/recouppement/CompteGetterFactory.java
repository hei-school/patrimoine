package school.hei.patrimoine.modele.recouppement;

import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Compte;

public class CompteGetterFactory {
  private CompteGetterFactory() {}

  public interface CompteGetter extends Function<String, Compte> {}

  public static CompteGetter make(Patrimoine patrimoine) {
    var comptes = getComptes(patrimoine);
    return compteNom ->
        findCompte(compteNom, comptes)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        String.format(
                            "%s n'a pas été trouvé lors du recoupement de %s",
                            compteNom, patrimoine.nom())));
  }

  public static CompteGetter make(Cas cas, Set<Compte> casSetComptes) {
    var casComptes = getComptes(cas.patrimoine());

    return (compteNom) -> {
      var optCasFromCompte = findCompte(compteNom, casComptes);
      return optCasFromCompte.orElseGet(
          () ->
              findCompte(compteNom, casSetComptes)
                  .orElseThrow(
                      () ->
                          new IllegalArgumentException(
                              String.format(
                                  "%s n'a pas été trouvé lors du recoupement de %s ",
                                  compteNom, cas.patrimoine().nom()))));
    };
  }

  public static Set<Compte> getComptes(Patrimoine patrimoine) {
    return patrimoine.getPossessions().stream()
        .filter(p -> p instanceof Compte)
        .map(Compte.class::cast)
        .collect(toSet());
  }

  public static Set<Compte> getComptes(CasSet casSet) {
    return casSet.set().stream()
        .map(cas -> cas.patrimoine().getPossessions())
        .flatMap(Set::stream)
        .filter(p -> p instanceof Compte)
        .map(Compte.class::cast)
        .collect(toSet());
  }

  public static Optional<Compte> findCompte(String compteNom, Collection<Compte> comptes) {
    return comptes.stream().filter(compte -> compte.nom().equals(compteNom)).findFirst();
  }
}
