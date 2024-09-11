package school.hei.patrimoine.visualisation.xchart;

import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.WHITE;
import static java.nio.file.Files.createTempFile;
import static java.util.UUID.randomUUID;
import static org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG;
import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;
import static org.knowm.xchart.style.Styler.LegendPosition.OutsideE;
import static org.knowm.xchart.style.markers.SeriesMarkers.NONE;
import static school.hei.patrimoine.visualisation.xchart.StyleSerie.SerieWidth.FAT;
import static school.hei.patrimoine.visualisation.xchart.StyleSerie.SerieWidth.NORMAL;
import static school.hei.patrimoine.visualisation.xchart.StyleSerie.SerieWidth.THIN;
import static school.hei.patrimoine.visualisation.xchart.StyleSerie.Stroke.CONTINUOUS;
import static school.hei.patrimoine.visualisation.xchart.StyleSerie.Stroke.DASH;

import java.awt.*;
import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.theme.MatlabTheme;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.possession.Creance;
import school.hei.patrimoine.modele.possession.Dette;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

public class GrapheurEvolutionPatrimoine implements Function<EvolutionPatrimoine, File> {

  private static final int DPI = 300;

  private static void configureSeries(EvolutionPatrimoine evolutionPatrimoine, XYChart chart) {
    var dates = evolutionPatrimoine.dates().toList();
    var seriesParPossession = serieValeursComptablesParPossession(evolutionPatrimoine);
    seriesParPossession
        .keySet()
        .forEach(
            possession ->
                addSerie(
                    chart,
                    possession.getNom(),
                    dates,
                    seriesParPossession.get(possession),
                    styleSerie(possession)));
    addSerie(
        chart,
        "Patrimoine",
        dates,
        serieValeursComptablesPatrimoine(evolutionPatrimoine),
        new StyleSerie(FAT, CONTINUOUS, false));
  }

  private static StyleSerie styleSerie(Possession possession) {
    if (possession instanceof Materiel) {
      return new StyleSerie(NORMAL, CONTINUOUS, true);
    }

    return possession instanceof Dette || possession instanceof Creance
        ? new StyleSerie(THIN, DASH, false)
        : new StyleSerie(NORMAL, CONTINUOUS, false);
  }

  private static Map<Possession, List<Integer>> serieValeursComptablesParPossession(
      EvolutionPatrimoine ep) {
    var map = new HashMap<Possession, List<Integer>>();

    for (var possession : ep.getPatrimoine().possessions()) {
      if (possession instanceof FluxArgent) {
        continue; // valeur comptable toujours 0
      }
      var serie = new ArrayList<Integer>();

      ep.dates()
          .forEach(
              d ->
                  serie.add(
                      ep.getEvolutionJournaliere()
                          .get(d)
                          .possessionParNom(possession.getNom())
                          .getValeurComptable()));
      map.put(possession, serie);
    }
    return map;
  }

  private static List<Integer> serieValeursComptablesPatrimoine(EvolutionPatrimoine ep) {
    var serie = new ArrayList<Integer>();
    ep.dates().forEach(d -> serie.add(ep.getEvolutionJournaliere().get(d).getValeurComptable()));
    return serie;
  }

  private static void addSerie(
      XYChart chart,
      String nom,
      List<LocalDate> localDates,
      List<Integer> values,
      StyleSerie style) {
    if (values.stream().allMatch(value -> value == 0)) {
      return;
    }

    var dates =
        localDates.stream()
            .map(localDate -> Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .toList();
    var serie = chart.addSeries(nom, dates, values);

    if (!style.withMarker()) {
      serie.setMarker(NONE);
    }
    serie.setMarkerColor(DARK_GRAY);
    serie.setSmooth(true);
    serie.setLineColor(color(nom));
    serie.setLineWidth(style.width().getValue());
    serie.setLineStyle(style.stroke().getValue());
  }

  private static Color color(String nom) {
    var nomEnNb = Arrays.stream(nom.split("")).mapToInt(s -> s.charAt(0)).sum();
    var r = nomEnNb % 255;
    var g = 255 - r;
    var b = (128 + nomEnNb) % 255;
    return new Color(r, g, b);
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
