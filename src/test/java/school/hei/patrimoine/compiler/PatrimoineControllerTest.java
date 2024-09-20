package school.hei.patrimoine.compiler;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.modele.Patrimoine;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineControllerTest {
    @Test
    void convertit_un_string_en_une_liste_de_patrimoine() throws Exception {

        String code = "import java.util.List;" +
                "import java.util.ArrayList;" +
                "import school.hei.patrimoine.modele.Patrimoine;" +
                "public class DynamicClass {" +
                "   public static Patrimoine compileCode(Patrimoine patrimoine) {" +
                "       " + "return patrimoine;" +
                "   }" +
                "}";

        PatrimoineCresusCas patrimoineCresusCas =
                new PatrimoineCresusCas();

        Patrimoine patrimoineCresus = patrimoineCresusCas.get();

        Patrimoine patrimoine = PatrimoineCompiler.stringCompiler(code);

        assertEquals(patrimoineCresus.getValeurComptable(), patrimoine.getValeurComptable());
    }
}
