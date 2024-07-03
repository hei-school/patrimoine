package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

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


    @Test
    void patrimoineDeZetyEntre17et18sept2024(){

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

        Patrimoine patrimoineDeZetyAu17sept2024=patrimoineDeZety.projectionFuture(au17sept2024);

        LocalDate au18sept2024=LocalDate.of(2024,Month.SEPTEMBER,18);

        Dette dette=new Dette("dette",au18sept2024,-11_000_000);
       // FluxArgent ajouterArgentPretéAuCompte=new FluxArgent("argent preté",compteBancaire,au18sept2024,au18sept2024,10_000_000,18);
        Argent argentPreté=new Argent("argentPreté",au18sept2024,10_000_000);
       Patrimoine patrimoineDeZetyAU18Sept2024= patrimoineDeZetyAu17sept2024.projectionFuture(au18sept2024);

        patrimoineDeZetyAU18Sept2024.possessions().add(dette);
        patrimoineDeZetyAU18Sept2024.possessions().add(argentPreté);

        Assertions.assertEquals(1002163,patrimoineDeZetyAu17sept2024.getValeurComptable()-patrimoineDeZetyAU18Sept2024.getValeurComptable());


    }
}
