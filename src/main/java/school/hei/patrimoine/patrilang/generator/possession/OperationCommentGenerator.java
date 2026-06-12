package school.hei.patrimoine.patrilang.generator.possession;

import school.hei.patrimoine.modele.possession.pj.OperationComment;
import school.hei.patrimoine.patrilang.generator.DatePatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.IdPatriLangGenerator;
import school.hei.patrimoine.patrilang.generator.PatriLangGenerator;

public class OperationCommentGenerator implements PatriLangGenerator<OperationComment> {
  private final IdPatriLangGenerator idGenerator;
  private final DatePatriLangGenerator dateGenerator;

  public OperationCommentGenerator() {
    this.idGenerator = new IdPatriLangGenerator();
    this.dateGenerator = new DatePatriLangGenerator();
  }

  @Override
  public String apply(OperationComment comment) {
    var id = this.idGenerator.apply(comment.id());
    var date = this.dateGenerator.apply(comment.date());

    return String.format("* `%s`, %s, \"%s\"", id, date, comment.content());
  }
}
