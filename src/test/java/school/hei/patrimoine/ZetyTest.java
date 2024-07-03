package school.hei.patrimoine;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ZetyTest {
    @Test
    public void zetyEtudieEn2023a2024(){
        var zety=new Personne("Zety");
        var t= LocalDate.of(2024,7,3);
        var futur=LocalDate.of(2024,9,17);
        var ordinateur=new Materiel("ordinateur",t,1200000,t,10d);
        var ordiFutur=ordinateur.projectionFuture(futur);
        var vetements=new Materiel("vetement",t,1500000,t,50d);
        var vetementsFutur=vetements.projectionFuture(futur);
        var argent=new Argent("argent",t,800000);
        var argentFutur=argent.projectionFuture(futur);
        var ecolage=new Argent("frais",t,-200000);
        var argentBancaire=new Argent("argent",t,100000);
        var fraisDeScolarite=new FluxArgent("zety",ecolage,LocalDate.of(2023,11,1),LocalDate.of(2024,8,31),-200000,27);
        var fraisFutur=fraisDeScolarite.projectionFuture(futur);
        var compteBancaire=new FluxArgent("compte",argentBancaire,t,LocalDate.of(2030,12,12),20000,25);
        var bancFutur=compteBancaire.projectionFuture(futur);
        Set<Possession> possessionSet=Set.of(ordinateur,vetements,argent,fraisDeScolarite,compteBancaire);
        Set<Possession> possessionFutur=Set.of(ordiFutur,vetementsFutur,argentFutur,fraisFutur,bancFutur);
        var patrimoineDeZety=new Patrimoine(zety.nom(), zety, LocalDate.of(2024,7,3),possessionSet);
        var patrimoineDeZetyFutur=new Patrimoine(zety.nom(), zety, futur,possessionFutur);
        var result=patrimoineDeZety.projectionFuture(futur);
        assertEquals(patrimoineDeZetyFutur,result);
        System.out.println(patrimoineDeZety.getValeurComptable());
        System.out.println(patrimoineDeZetyFutur.getValeurComptable());
        assertEquals(patrimoineDeZetyFutur.getValeurComptable(),result.getValeurComptable());
    }
    @Test
    public  void zetySenDette(){
        var zety=new Personne("Zety");
        var t=LocalDate.of(2024,9,18);
        var t17=LocalDate.of(2024,9,17);
        var dateDuEndettement=LocalDate.of(2024,9,18);
        var argentBancaire=new Argent("argent",t,10000000);

        var compteBancaire=new FluxArgent("compte",argentBancaire,t,LocalDate.of(2030,12,12),20000,25);
        var detteZety=new Dette("zety",dateDuEndettement,-11000000);

        Set<Possession> possessionSet=Set.of(compteBancaire,detteZety);
        Set<Possession> possessionFutur=Set.of(argentBancaire.projectionFuture(t),detteZety.projectionFuture(t));
        var patrimoineDeZety=new Patrimoine(zety.nom(),zety,t,possessionSet);
        var result=patrimoineDeZety.projectionFuture(t);
        var result17septembre=patrimoineDeZety.projectionFuture(t17);
        assertEquals(true,result.getValeurComptable()==patrimoineDeZety.getValeurComptable());
        assertEquals(true,result17septembre.getValeurComptable()>patrimoineDeZety.getValeurComptable());
    }
    @Test
    public void zetyEtudieEn2024a2025(){
        var t=LocalDate.of(2024,7,3);
        var futur=LocalDate.of(2024,9,17);
        var ordinateur=new Materiel("ordinateur",t,1200000,t,10d);
        var vetements=new Materiel("vetement",t,1500000,t,50d);
        var argent=new Argent("argent",t,800000);
        var ecolage=new Argent("frais",t,-200000);
        var argentBancaire=new Argent("argent",t,100000);
        var fraisDeScolarite=new FluxArgent("zety",ecolage,LocalDate.of(2023,11,1),LocalDate.of(2024,8,31),-200000,27);
        var compteBancaire=new FluxArgent("compte",argentBancaire,t,LocalDate.of(2030,12,12),20000,25);
        var zety=new Personne("Zety");
        var septembre212024=LocalDate.of(2024,9,21);
        var fraisDeScolarite2025=new FluxArgent("zety",ecolage,LocalDate.of(2023,11,1),LocalDate.of(2024,8,31),-250000,27);
        var compte=new Argent("zety",septembre212024,0);
        var transfertDargentParent=new FluxArgent(zety.nom(),compte,LocalDate.of(2024,1,1),LocalDate.of(2025,12,12),100000,15);
        var trainDeVie=new FluxArgent(zety.nom(),compte,LocalDate.of(2024,10,1),LocalDate.of(2025,2,13),250000,1);
        Set<Possession> possessionSet=new HashSet<>();
        possessionSet.add(ordinateur);
        possessionSet.add(vetements);
        possessionSet.add(argent);
        possessionSet.add(fraisDeScolarite);
        possessionSet.add(compteBancaire);
        possessionSet.add(fraisDeScolarite2025);
        possessionSet.add(compte);
        possessionSet.add(trainDeVie);
        possessionSet.add(transfertDargentParent);
        var patrimoineDeZety=new Patrimoine(zety.nom(), zety, LocalDate.of(2024,1,1),possessionSet);
        var start=LocalDate.of(2024,1,1);
        var end=LocalDate.of(2025,12,12);
        var result=LocalDate.of(2024,1,1);
        for (LocalDate date  = start; !date.isAfter(end) ; date=date.plusDays(1)) {
            if(patrimoineDeZety.projectionFuture(date).getValeurComptable()==0){
                result=date;
            }
        }
        assertEquals(0,patrimoineDeZety.projectionFuture(LocalDate.of(2024,7,2)).getValeurComptable());
    }
    @Test
    public void zetyPartEnAllemagne(){
        var t=LocalDate.of(2024,7,3);
        var futur=LocalDate.of(2024,9,17);
        var ordinateur=new Materiel("ordinateur",t,1200000,t,10d);
        var vetements=new Materiel("vetement",t,1500000,t,50d);
        var argent=new Argent("argent",t,800000);
        var ecolage=new Argent("frais",t,-200000);
        var argentBancaire=new Argent("argent",t,100000);
        var fraisDeScolarite=new FluxArgent("zety",ecolage,LocalDate.of(2023,11,1),LocalDate.of(2024,8,31),-200000,27);
        var compteBancaire=new FluxArgent("compte",argentBancaire,t,LocalDate.of(2030,12,12),20000,25);
        var zety=new Personne("Zety");
        var septembre212024=LocalDate.of(2024,9,21);
        var fraisDeScolarite2025=new FluxArgent("zety",ecolage,LocalDate.of(2023,11,1),LocalDate.of(2024,8,31),-250000,27);
        var compte=new Argent("zety",septembre212024,0);
        var transfertDargentParent=new FluxArgent(zety.nom(),compte,LocalDate.of(2024,1,1),LocalDate.of(2025,12,12),100000,15);
        var trainDeVie=new FluxArgent(zety.nom(),compte,LocalDate.of(2024,10,1),LocalDate.of(2025,2,13),250000,1);
        Set<Possession> possessionSet=new HashSet<>();
        possessionSet.add(ordinateur);
        possessionSet.add(vetements);
        possessionSet.add(argent);
        possessionSet.add(fraisDeScolarite);
        possessionSet.add(compteBancaire);
        possessionSet.add(fraisDeScolarite2025);
        possessionSet.add(compte);
        possessionSet.add(trainDeVie);
        possessionSet.add(transfertDargentParent);
        var patrimoineDeZety=new Patrimoine(zety.nom(), zety, LocalDate.of(2024,1,1),possessionSet);
        System.out.println(patrimoineDeZety.projectionFuture(LocalDate.of(2025,2,14)).getValeurComptable());
        var dettee=7000*4821;
        var nouveauDetteEnAllemagne=new Dette(zety.nom(), LocalDate.of(2024,2,15),dettee);
        possessionSet.add(nouveauDetteEnAllemagne);
        assertEquals(57768492,patrimoineDeZety.projectionFuture(LocalDate.of(2025,2,14)).getValeurComptable());
    }

}

