package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;

public class PatriLangVisitorFactory {
  public static PatriLangVisitor create(SectionVisitor sectionVisitor) {
    var toutCasVisitor = new PatriLangToutCasVisitor(sectionVisitor);
    var casVisitor = new PatriLangCasVisitor(sectionVisitor);
    return new PatriLangVisitor(toutCasVisitor, casVisitor);
  }
}
