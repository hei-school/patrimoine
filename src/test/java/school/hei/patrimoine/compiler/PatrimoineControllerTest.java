package school.hei.patrimoine.compiler;

import org.junit.jupiter.api.Test;
import school.hei.patrimoine.cas.PatrimoineCresusCas;
import school.hei.patrimoine.modele.Patrimoine;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PatrimoineControllerTest {
    @Test
    void convertit_un_string_en_une_liste_de_patrimoine() throws Exception {

        String code = "import java.util.List;" +
                "import java.util.ArrayList;" +
                "import school.hei.patrimoine.modele.Patrimoine;" +
                "public class DynamicClass {" +
                "   public static List<Patrimoine> compileCode(Patrimoine patrimoine) {" +
                "       " + "return List.of(patrimoine);" +
                "   }" +
                "}";

        PatrimoineCresusCas patrimoineCresusCas =
                new PatrimoineCresusCas();

        Patrimoine patrimoineCresus = patrimoineCresusCas.get();

        List<Patrimoine> patrimoines = PatrimoineCompiler.stringCompiler(code);

        assertEquals(patrimoineCresus.getValeurComptable(), patrimoines.getFirst().getValeurComptable());
    }
}
