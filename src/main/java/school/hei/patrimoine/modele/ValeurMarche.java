package school.hei.patrimoine.modele;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;

@ToString
@AllArgsConstructor
public class ValeurMarche implements Serializable {
  @Accessors(fluent = true)
  @Getter
  private final LocalDate date;
  @Accessors(fluent = true)
  @Getter
  private final Argent valeur;
}