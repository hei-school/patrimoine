package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Patrimoine17Sept24 {
    @Test
     void testValeurPatrimoineAu17Septembre2024() {
        LocalDate dateReference = LocalDate.of(2024, 7, 3);
        LocalDate dateFuture = LocalDate.of(2024, 9, 17);

        Materiel ordinateur = new Materiel("Ordinateur", dateReference, 1200000, dateReference, -0.10);
        Possession ordinateurFutur = ordinateur.projectionFuture(dateFuture);

        Materiel vetements = new Materiel("Vêtements", dateReference, 1500000, dateReference, -0.50);
        Possession vetementsFutur = vetements.projectionFuture(dateFuture);

        Argent argentEspeces = new Argent("Espèces", dateReference, 800000);

        LocalDate debutScolarite = LocalDate.of(2023, 11, 27);
        LocalDate finScolarite = LocalDate.of(2024, 8, 27);
        FluxArgent fraisScolarite = new FluxArgent("Frais de scolarité", argentEspeces, debutScolarite, finScolarite, -200000, 27);
        Argent argentEspecesFutur = argentEspeces.projectionFuture(dateFuture);

        Argent compteBancaire = new Argent("Compte Bancaire", dateReference, 100000);
        FluxArgent fraisCompte = new FluxArgent("Frais de compte", compteBancaire, dateReference, LocalDate.of(9999, 12, 31), -20000, 25);
        Argent compteBancaireFutur = compteBancaire.projectionFuture(dateFuture);

        int patrimoineTotal = ordinateurFutur.getValeurComptable()
                + vetementsFutur.getValeurComptable()
                + argentEspecesFutur.getValeurComptable()
                + compteBancaireFutur.getValeurComptable();

        assertEquals(2978848, patrimoineTotal);
    }
}
