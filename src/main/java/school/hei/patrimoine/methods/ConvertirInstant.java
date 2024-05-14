package school.hei.patrimoine.methods;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ConvertirInstant {
    public int extraireLAnneeDIstant(Instant instant){
        ZonedDateTime instantConverti = instant.atZone(ZoneId.systemDefault());
        int annee = instantConverti.getYear();
        return annee;
    }
}
