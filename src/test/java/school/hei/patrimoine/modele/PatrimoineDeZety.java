package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

public class PatrimoineDeZety {
    @Test
    void patrimoineDeZetyAuDateDu3Jullet2024(){
        Personne zety=new Personne("Zety");
        LocalDate au03juillet2024=LocalDate.of(2024, Month.JULY,3);
        Materiel ordinateur=new Materiel("ordinateur",au03juillet2024,
                1_200_000,au03juillet2024,-0.1);
        Materiel vetements=new Materiel("vetements",au03juillet2024,
                1_500_000,au03juillet2024.minusDays(10),
                -0.5
                );
        Argent espece=new Argent("espece",au03juillet2024,800_000);

        LocalDate novembre2023=LocalDate.of(2023,Month.NOVEMBER,1);
        LocalDate aout2024=LocalDate.of(2024,Month.AUGUST,31);
        FluxArgent fraisDeScolarité=new FluxArgent("frais de scolarité",
                espece,novembre2023,aout2024,-200_000,27);
        Argent compteBancaire=new Argent("compte",au03juillet2024,100_000);
        FluxArgent fraisDeTenueDeCompte=new FluxArgent("frais de tenue de compte",
                compteBancaire,au03juillet2024,LocalDate.of(2024,Month.SEPTEMBER,17),-20_000,25);

        LocalDate au17sept2024=LocalDate.of(2024,Month.SEPTEMBER,17);
        Patrimoine patrimoineDeZety=new Patrimoine("patrimoine de zety",zety,
                au03juillet2024, Set.of(
                        ordinateur,
                        vetements,
                        espece,
                        fraisDeScolarité,
                        compteBancaire,
                        fraisDeTenueDeCompte
        ));

        Assertions.assertEquals(2_978_848,patrimoineDeZety.projectionFuture(au17sept2024).getValeurComptable());
    }
}
