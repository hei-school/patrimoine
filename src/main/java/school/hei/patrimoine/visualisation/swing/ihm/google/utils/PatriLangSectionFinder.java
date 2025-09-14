package school.hei.patrimoine.visualisation.swing.ihm.google.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.patrilang.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;


public class PatriLangSectionFinder<T extends ParserRuleContext> implements BiFunction<String, Function<DocumentContext, T>, Optional<T>> {
    @Override
    public Optional<T> apply(String filePath, Function<DocumentContext, T> getter) {
        var document = PatriLangParser.parse(filePath);
        return Optional.ofNullable(getter.apply(document));
    }
}
