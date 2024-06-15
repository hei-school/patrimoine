package school.hei.patrimoine.visualisation;

import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.theme.MatlabTheme;
import school.hei.patrimoine.modele.EvolutionPatrimoine;

import java.io.File;
import java.text.DecimalFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.function.Function;

import static java.awt.Color.WHITE;
import static java.nio.file.Files.createTempFile;
import static java.util.UUID.randomUUID;
import static org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG;
import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;
import static org.knowm.xchart.XYSeries.XYSeriesRenderStyle.Line;
import static org.knowm.xchart.style.Styler.LegendPosition.OutsideE;

public class Visualiseur implements Function<EvolutionPatrimoine, File> {

  private static final int DPI = 300;

  @SneakyThrows
  @Override
  public File apply(EvolutionPatrimoine evolutionPatrimoine) {
    var chart = new XYChartBuilder()
        .width(800)
        .height(600)
        .build();
    configureStyle(chart);
    var dates = evolutionPatrimoine.dates()
        .map(localDate -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        .toList();
    var seriesParPossession = evolutionPatrimoine.serieValeursComptablesParPossession();
    seriesParPossession.keySet().forEach(
        possession -> chart.addSeries(possession.getNom(), dates, seriesParPossession.get(possession)));
    chart.addSeries(
        "Patrimoine",
        dates,
        evolutionPatrimoine.serieValeursComptablesPatrimoine());

    var temp = createTempFile(randomUUID().toString(), ".png").toFile();
    saveBitmapWithDPI(chart, temp.getAbsolutePath(), PNG, DPI);
    System.out.println("Image générée: " + temp.getAbsolutePath());

    return temp;
  }

  private void configureStyle(XYChart chart) {
    var styler = chart.getStyler();
    styler.setPlotBackgroundColor(WHITE);
    styler.setChartBackgroundColor(WHITE);
    styler.setLegendPosition(OutsideE);
    styler.setDefaultSeriesRenderStyle(Line);
    styler.setyAxisTickLabelsFormattingFunction(this::yFormatter);
    styler.setPlotMargin(0);
    styler.setTheme(new MatlabTheme());
  }

  private String yFormatter(Double valeurComptable) {
    var decimalFormatter = new DecimalFormat("###,###,###");
    var symbols = decimalFormatter.getDecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    decimalFormatter.setDecimalFormatSymbols(symbols);
    return decimalFormatter.format(valeurComptable);
  }
}
