package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static java.time.Month.AUGUST;
import static java.time.Month.JULY;
import static java.time.Month.NOVEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransfertArgentTest {
    @Test
    void fraisScolarite(){
        var debutScolarite = LocalDate.of(2023, NOVEMBER, 1);
        var argent = new Argent("Espèces", debutScolarite, 0);
        var fraisMensuel = -200_000;
        var donsMensuel = 200_000;
        var dateOperation = 27;

        var dateFuture = LocalDate.of(2024, JULY, 3);

        var expectedValeurComptable = 0;

        expectedValeurComptable += fraisMensuel * 8;
        expectedValeurComptable += donsMensuel * 8;

        if (dateFuture.getDayOfMonth() >= dateOperation) {
            expectedValeurComptable += fraisMensuel;
            expectedValeurComptable += donsMensuel;
        }
        assertEquals(expectedValeurComptable, argent.projectionFuture(dateFuture).getValeurComptable());
    }
}
