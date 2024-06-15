package school.hei.patrimoine;

import java.io.Serializable;

public record Personne(String nom) implements Serializable/*note(no-serializable)*/ {
}
