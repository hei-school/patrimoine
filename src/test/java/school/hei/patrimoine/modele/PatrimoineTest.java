package school.hei.patrimoine.modele;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.modele.possession.*;

import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.MAY;
import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineTest {

  @Test
  void patrimoine_vide_vaut_0() {
    var ilo = new Personne("Ilo");

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        LocalDate.of(2024, MAY, 13),
        Set.of());

    assertEquals(0, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_a_de_l_argent() {
    var ilo = new Personne("Ilo");

    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(
            new Argent("Espèces", au13mai24, 400_000),
            new Argent("Compte epargne", au13mai24, 200_000),
            new Argent("Compte courant", au13mai24, 600_000)));

    assertEquals(1_200_000, patrimoineIloAu13mai24.getValeurComptable());
  }

  @Test
  void patrimoine_possede_un_train_de_vie_financé_par_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(financeur, trainDeVie));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }

  @Test
  void patrimoine_possede_groupe_de_train_de_vie_et_d_argent() {
    var ilo = new Personne("Ilo");
    var au13mai24 = LocalDate.of(2024, MAY, 13);
    var financeur = new Argent("Espèces", au13mai24, 600_000);
    var trainDeVie = new FluxArgent(
        "Vie courante",
        financeur, au13mai24.minusDays(100), au13mai24.plusDays(100), -100_000,
        15);

    var patrimoineIloAu13mai24 = new Patrimoine(
        "patrimoineIloAu13mai24",
        ilo,
        au13mai24,
        Set.of(new GroupePossession("Le groupe", au13mai24, Set.of(financeur, trainDeVie))));

    assertEquals(500_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(10)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(100)).getValeurComptable());
    assertEquals(200_000, patrimoineIloAu13mai24.projectionFuture(au13mai24.plusDays(1_000)).getValeurComptable());
  }


  @Test
  void patrimoine_zety_le_17_septembre_2024() {
    var au17Septembre = LocalDate.of(2024, 9, 17);
    var ordinateur = new Materiel("Ordinateur", LocalDate.of(2024, 7, 3), 1_200_000, LocalDate.of(2024, 7, 3), -0.10);
    var vetements = new Materiel("Vêtements", LocalDate.of(2024, 7, 3), 1_500_000, LocalDate.of(2024, 7, 3), -0.50);
    var argentEnEspece = new Argent("Argent en espèces", LocalDate.of(2024, 7, 3), 800_000);
    var compteBancaire = new Argent("Compte bancaire", LocalDate.of(2024, 7, 3), 100_000);
    var fraisTenueCompte = new FluxArgent("Frais de tenue de compte", compteBancaire, LocalDate.of(2024, 7, 3), LocalDate.of(2024, 7, 3).plusMonths(12), -20_000, 25);
    var fraisScolarite = new Argent("Frais de scolarité", LocalDate.of(2024, 7, 3), 0);
    var fluxFraisScolarite = new FluxArgent("Frais de scolarité", fraisScolarite, LocalDate.of(2023, 11, 27), LocalDate.of(2024, 8, 27), -200_000, 27);

    var patrimoineZety = new Patrimoine("Patrimoine Zety", new Personne("Zety"), LocalDate.of(2024, 7, 3), Set.of(ordinateur, vetements, argentEnEspece, compteBancaire, fraisScolarite, fluxFraisScolarite, fraisTenueCompte));

    var projectionFuture = patrimoineZety.projectionFuture(au17Septembre);
    assertEquals(2978848, projectionFuture.getValeurComptable());
  }

  @Test
  public void dette_de_zety() {
    var Zety = new Personne("Zety");
    var au3Juillet24 = LocalDate.of(2024, JULY, 3);
    var au17Sept24 = LocalDate.of(2024, SEPTEMBER, 17);
    var au18Sept24 = LocalDate.of(2024, SEPTEMBER, 18);

    var ordinateur = new Materiel(
            "ordinateur de zety",
            au3Juillet24,
            1_200_000,
            au3Juillet24,
            -0.10
    );
    var vetements = new Materiel(
            "vetements de zety",
            au3Juillet24,
            1_500_000,
            au3Juillet24,
            -0.50
    );
    var argent_en_espece = new Argent("Argent en espece de Zety", au3Juillet24, 800_000);
    var argent_ecollage_de_zety = new Argent("Ecollage de Zety", au3Juillet24, 200_000);
    var argent_dans_le_compte = new Argent("Argent dans le compte de Zety", au3Juillet24, 100_000);
    var frais_de_scolarite = new FluxArgent("Frais de scolarite de Zety", argent_ecollage_de_zety, LocalDate.of(2023, NOVEMBER, 1), LocalDate.of(2024, AUGUST, 31), -200_000, 27);
    var compte_bancaire = new FluxArgent("Compte bancaire de Zety", argent_dans_le_compte, au3Juillet24, au17Sept24, -20_000, 25);
    var emprunt = new Argent("Emprunt de Zety", au18Sept24, 10_000_000);
    var dette = new Argent("Dette de Zety", au18Sept24, -11_000_000);
    var patrimoine_Zety_le_3_juillet_2024 = new Patrimoine(
            "patrimoine de zety 17 septembre 2024",
            Zety,
            au3Juillet24,
            Set.of(new GroupePossession(
                            "possession de Zety",
                            au3Juillet24,
                            Set.of(ordinateur,
                                    vetements,
                                    argent_en_espece,
                                    frais_de_scolarite,
                                    compte_bancaire,
                                    emprunt,
                                    dette
                            )
                    )
            )
    );

    var patrimoine_le_17_septembre = patrimoine_Zety_le_3_juillet_2024.projectionFuture(au17Sept24).getValeurComptable();

    var patrimoine_le_18_septembre = patrimoine_Zety_le_3_juillet_2024.projectionFuture(au18Sept24).getValeurComptable();

    var diminution_de_patimoine = patrimoine_le_17_septembre - patrimoine_le_18_septembre;

    assertEquals(1002384, diminution_de_patimoine);
  }

  @Test
  void zety_etudie_en_2024() {
    int argentInitial = 0;
    int fraisScolarite = 2_500_000;
    int donMensuel = 100_000;
    int trainDeVieMensuel = 250_000;

    int argentDisponible = argentInitial;
    argentDisponible -= fraisScolarite;
    argentDisponible += donMensuel;
    argentDisponible += donMensuel;
    argentDisponible -= trainDeVieMensuel;
    argentDisponible += donMensuel;
    argentDisponible -= trainDeVieMensuel;
    argentDisponible += donMensuel;
    argentDisponible -= trainDeVieMensuel;

    argentDisponible += donMensuel;
    argentDisponible -= trainDeVieMensuel;

    assertEquals(-3_000_000, argentDisponible, "zety espce");
  }



}














