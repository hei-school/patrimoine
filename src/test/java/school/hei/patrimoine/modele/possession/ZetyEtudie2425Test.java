package school.hei.patrimoine.modele.possession;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class ZetyEtudie2425Test {
    @Test
    public void testCashBalanceSimulation() {
        LocalDate startDate = LocalDate.of(2024, 9, 21);
        LocalDate currentDate = startDate;
        int cashBalance = -2500000;

        while (currentDate.isBefore(LocalDate.of(2025, 2, 14))) {
            if (currentDate.getDayOfMonth() == 15) {
                cashBalance += 100000;
            }

            if (currentDate.getDayOfMonth() == 1 &&
                    currentDate.isAfter(LocalDate.of(2024, 9, 30)) &&
                    currentDate.isBefore(LocalDate.of(2025, 2, 14))) {
                cashBalance -= 250000;
            }

            if (cashBalance < 0) {
                System.out.println("Zety runs out of cash on: " + currentDate.toString());
                break;
            }

            currentDate = currentDate.plusDays(1);
        }

        assertTrue(currentDate.isBefore(LocalDate.of(2025, 2, 14)));
    }

}
