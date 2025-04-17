package school.hei.patrimoine.patrilang;

import school.hei.patrimoine.patrilang.antlr.PatriLangParser;
import school.hei.patrimoine.patrilang.antlr.PatriLangParserBaseVisitor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class PatriLangTranspileVisitor extends PatriLangParserBaseVisitor<Map<String, String>> {
    private Map<String, String> map = new HashMap<>();
    @Override
    public Map<String, String> visitDocument(PatriLangParser.DocumentContext ctx) {
        return this.visitSectionGeneral(ctx.sectionGeneral());
    }

    @Override
    public Map<String, String> visitDate(PatriLangParser.DateContext ctx) {
        LocalDate localDate = LocalDate.of(
            parseInt(ctx.NOMBRE(2).getText()),
            parseInt(ctx.NOMBRE(1).getText()),
            parseInt(ctx.NOMBRE(0).getText())
        );

        String date = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.map.put("Date", date);
        return this.map;
    }

    @Override
    public Map<String, String> visitLignePatrimoineDevise(PatriLangParser.LignePatrimoineDeviseContext ctx) {
        this.map.put("Devise", ctx.DEVISE().getText());
        return this.map;
    }

    @Override
    public Map<String, String> visitLignePatrimoineNom(PatriLangParser.LignePatrimoineNomContext ctx) {
        this.map.put("Nom", ctx.TEXT().getText());
        return this.map;
    }


    @Override
    public Map<String, String> visitSectionGeneral(PatriLangParser.SectionGeneralContext ctx) {
        this.visitLignePatrimoineDate(ctx.lignePatrimoineDate());
        this.visitLignePatrimoineNom(ctx.lignePatrimoineNom());
        this.visitLignePatrimoineDevise(ctx.lignePatrimoineDevise());
        return this.map;
    }
}
