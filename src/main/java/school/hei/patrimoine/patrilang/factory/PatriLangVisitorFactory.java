package school.hei.patrimoine.patrilang.factory;

import school.hei.patrimoine.patrilang.visitors.*;

public class PatriLangVisitorFactory {
  public static PatriLangVisitor make(SectionVisitor sectionVisitor) {
    var toutCasVisitor = new PatriLangToutCasVisitor(sectionVisitor);
    var casVisitor = new PatriLangCasVisitor(sectionVisitor.getDateVisitor(), sectionVisitor);
    return new PatriLangVisitor(toutCasVisitor, casVisitor);
  }
}
