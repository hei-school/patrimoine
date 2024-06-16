package school.hei.patrimoine.visualisation.xchart;

import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.theme.MatlabTheme;
import school.hei.patrimoine.modele.EvolutionPatrimoine;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import static java.awt.Color.WHITE;
import static java.nio.file.Files.createTempFile;
import static java.util.UUID.randomUUID;
import static org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG;
import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;
import static org.knowm.xchart.style.Styler.LegendPosition.OutsideE;
import static org.knowm.xchart.style.markers.SeriesMarkers.NONE;

public class GrapheurEvolutionPatrimoine implements Function<EvolutionPatrimoine, File> {

  private static final int DPI = 300;

  private static void configureSeries(EvolutionPatrimoine evolutionPatrimoine, XYChart chart) {
    var dates = evolutionPatrimoine.dates().toList();
    var seriesParPossession = evolutionPatrimoine.serieValeursComptablesParPossession();
    seriesParPossession.keySet().forEach(
        possession -> addSerie(chart, possession.getNom(), dates, seriesParPossession.get(possession)));
    addSerie(
        chart,
        "Patrimoine",
        dates,
        evolutionPatrimoine.serieValeursComptablesPatrimoine());
  }

  private static void addSerie(XYChart chart, String nom, List<LocalDate> localDates, List<Integer> values) {
    if (values.stream().allMatch(value -> value == 0)) {
      return;
    }

    var dates = localDates.stream()
        .map(localDate -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
        .toList();
    var serie = chart.addSeries(nom, dates, values);
    serie.setMarker(NONE);
  }

  @SneakyThrows
  @Override
  public File apply(EvolutionPatrimoine evolutionPatrimoine) {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    configureStyle(chart);
    configureSeries(evolutionPatrimoine, chart);

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
    styler.setyAxisTickLabelsFormattingFunction(this::yFormatter);
    styler.setPlotMargin(0);
    styler.setTheme(new MatlabTheme());

    chart.getSeriesMap().values().forEach(serie -> serie.setMarker(NONE));
  }

  private String yFormatter(Double valeurComptable) {
    var decimalFormatter = new DecimalFormat("###,###,###");
    var symbols = decimalFormatter.getDecimalFormatSymbols();
    symbols.setGroupingSeparator(' ');
    decimalFormatter.setDecimalFormatSymbols(symbols);
    return decimalFormatter.format(valeurComptable);
  }
}
