package school.hei.patrimoine.modele.recouppement.model;

import static java.time.LocalDate.now;
import static java.util.stream.Collectors.toSet;
import static school.hei.patrimoine.modele.Argent.ariary;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import school.hei.patrimoine.cas.Cas;
import school.hei.patrimoine.cas.CasSet;
import school.hei.patrimoine.modele.possession.Compte;

@Slf4j
@RequiredArgsConstructor
public class CompteGetter implements Function<String, Compte> {
  private final Map<String, Compte> comptes;
  private final boolean throwIfNotFound;

  public CompteGetter(Set<Compte> comptes) {
    this(comptes, true);
  }

  public CompteGetter(Set<Compte> comptes, boolean throwIfNotFound) {
    this.throwIfNotFound = throwIfNotFound;
    this.comptes = new HashMap<>();
    for (var compte : comptes) {
      this.comptes.put(compte.nom(), compte);
    }
  }

  @Override
  public Compte apply(String nom) {
    if (comptes.containsKey(nom)) {
      return comptes.get(nom);
    }

    var message = String.format("%s n'a pas été trouvé lors du recoupement", nom);
    if (throwIfNotFound) {
      throw new IllegalArgumentException(message);
    }

    log.warn(message);
    comptes.put(nom, new Compte(nom, now(), ariary(0)));
    return comptes.get(nom);
  }

  public static CompteGetter make(Cas cas, Set<Compte> casSetComptes) {
    return new CompteGetter(Set.of()) {
      @Override
      public Compte apply(String nom) {
        try {
          var casCompteGetter = make(cas);
          return casCompteGetter.apply(nom);
        } catch (IllegalArgumentException exception) {
          var casSetCompteGetter = new CompteGetter(casSetComptes, false);
          return casSetCompteGetter.apply(nom);
        }
      }
    };
  }

  public static CompteGetter make(Cas cas) {
    return new CompteGetter(getComptes(cas));
  }

  public static CompteGetter make(CasSet casSet) {
    return new CompteGetter(getComptes(casSet));
  }

  public static Set<Compte> getComptes(CasSet casSet) {
    return casSet.set().stream()
        .map(cas -> cas.patrimoine().getPossessions())
        .flatMap(Set::stream)
        .filter(p -> p instanceof Compte)
        .map(Compte.class::cast)
        .collect(toSet());
  }

  public static Set<Compte> getComptes(Cas cas) {
    return cas.patrimoine().getPossessions().stream()
        .filter(p -> p instanceof Compte)
        .map(Compte.class::cast)
        .collect(toSet());
  }
}
