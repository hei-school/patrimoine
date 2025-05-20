package school.hei.patrimoine.patrilang;

import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.*;
import static school.hei.patrimoine.patrilang.antlr.PatriLangParser.ToutCasContext;

import java.time.LocalDate;
import java.util.function.Function;
import school.hei.patrimoine.cas.CasSetSupplier;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.modele.possession.Compte;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.patrilang.visitors.*;

public class PatriLangToutCasVisitor implements Function<ToutCasContext, CasSetSupplier> {
  private final SectionVisitor sectionVisitor;
  private final VariableVisitor<DateContext, LocalDate> variableDateVisitor;
  private final VariableVisitor<TextContext, Personne> variablePersonneVisitor;
  private final VariableVisitor<CompteContext, Compte> variableCompteVisitor;
  private final VariableVisitor<CompteContext, Dette> variableDetteVisitor;
  private final VariableVisitor<CompteContext, Creance> variableCreanceVisitor;

  public PatriLangToutCasVisitor() {
    this.variableDateVisitor = new VariableVisitor<>(DateContext.class, BaseVisitor::visitDate);
    this.variablePersonneVisitor =
        new VariableVisitor<>(TextContext.class, BaseVisitor::visitPersonne);

    var baseCompteVisitor = new CompteVisitor(this.variableDateVisitor);
    var baseCreanceVisitor = new CreanceVisitor(this.variableDateVisitor);
    var baseDetteVisitor = new DetteVisitor(this.variableDateVisitor);
    this.variableCompteVisitor = new VariableVisitor<>(CompteContext.class, baseCompteVisitor);
    this.variableCreanceVisitor = new VariableVisitor<>(CompteContext.class, baseCreanceVisitor);
    this.variableDetteVisitor = new VariableVisitor<>(CompteContext.class, baseDetteVisitor);

    this.sectionVisitor =
        new SectionVisitor(
            new MaterielVisitor(this.variableDateVisitor),
            new AchatMaterielVisitor(this.variableDateVisitor, this.variableCompteVisitor),
            new FluxArgentVisitor(this.variableDateVisitor, this.variableCompteVisitor),
            new TransferArgentVisitor(this.variableDateVisitor, this.variableCompteVisitor),
            new GroupPossessionVisitor(this.variableDateVisitor),
            this.variableDateVisitor,
            this.variablePersonneVisitor,
            this.variableCompteVisitor,
            this.variableDetteVisitor,
            this.variableCreanceVisitor);
  }

  public CasSetSupplier apply(ToutCasContext ctx) {
    return new CasSetSupplier();
  }
}
