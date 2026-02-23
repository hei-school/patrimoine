package school.hei.patrimoine.patrilang.files.io;

import java.util.Optional;
import java.util.function.Function;
import lombok.Builder;
import org.antlr.v4.runtime.ParserRuleContext;
import school.hei.patrimoine.patrilang.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParser.DocumentContext;
import school.hei.patrimoine.patrilang.files.PatriLangFile;

public class PatriLangFileQuerier {
  public <T extends ParserRuleContext> Optional<QueryResult<T>> query(
      PatriLangFile file, Function<DocumentContext, T> querier) {
    var document = PatriLangParser.parse(file);
    var context = querier.apply(document);

    if (context == null) {
      return Optional.empty();
    }

    return Optional.of(
        QueryResult.<T>builder()
            .context(context)
            .endLine(context.getStop().getLine())
            .startLine(context.getStart().getLine())
            .build());
  }

  @Builder
  public record QueryResult<T extends ParserRuleContext>(T context, int startLine, int endLine) {}
}
