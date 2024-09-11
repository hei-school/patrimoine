package school.hei.patrimoine.visualisation.xchart;

import static java.awt.Color.BLACK;
import static java.awt.Color.DARK_GRAY;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.Color.WHITE;
import static java.awt.Color.YELLOW;
import static java.nio.file.Files.createTempFile;
import static java.util.Comparator.comparing;
import static java.util.UUID.randomUUID;
import static org.knowm.xchart.BitmapEncoder.BitmapFormat.PNG;
import static org.knowm.xchart.BitmapEncoder.saveBitmapWithDPI;
import static org.knowm.xchart.style.Styler.LegendPosition.OutsideE;
import static org.knowm.xchart.style.markers.SeriesMarkers.NONE;
import static school.hei.patrimoine.modele.possession.TypeAgregat.IMMOBILISATION;
import static school.hei.patrimoine.modele.possession.TypeAgregat.OBLIGATION;
import static school.hei.patrimoine.modele.possession.TypeAgregat.TRESORIE;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.BiFunction;
import lombok.SneakyThrows;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.theme.MatlabTheme;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.possession.Possession;

public class GrapheurEvolutionPatrimoine implements BiFunction<EvolutionPatrimoine, Boolean, File> {

  private static final int DPI = 300;

  private static void configureSeries(
      EvolutionPatrimoine ep, XYChart chart, boolean withAggregates) {
    var dates = ep.serieDates();
    var seriesParPossession = ep.serieValeursComptablesParPossession();
    seriesParPossession.keySet().stream()
        .sorted(comparing(Possession::getNom))
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
        ep.serieValeursComptablesPatrimoine(),
        new StyleSerie(GREEN, FAT, CONTINUOUS, false));
    if (withAggregates) {
      addSerie(
          chart,
          "Immobilisation",
          dates,
          ep.serieParPossessionsFiltrées(p -> IMMOBILISATION.equals(p.typeAgregat())),
          new StyleSerie(BLACK, FAT, CONTINUOUS, false));
      addSerie(
          chart,
          "Trésorerie",
          dates,
          ep.serieParPossessionsFiltrées(p -> TRESORIE.equals(p.typeAgregat())),
          new StyleSerie(RED, FAT, CONTINUOUS, false));
      addSerie(
          chart,
          "Obligations",
          dates,
          ep.serieParPossessionsFiltrées(p -> OBLIGATION.equals(p.typeAgregat())),
          new StyleSerie(YELLOW, FAT, DASH, false));
    }
  }

  private static StyleSerie styleSerie(Possession possession) {
    var typeAgregat = possession.typeAgregat();
    if (IMMOBILISATION.equals(typeAgregat)) {
      return new StyleSerie(null, NORMAL, CONTINUOUS, true);
    }

    return OBLIGATION.equals(typeAgregat)
        ? new StyleSerie(null, THIN, DASH, false)
        : new StyleSerie(null, NORMAL, CONTINUOUS, false);
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
    serie.setLineColor(style.color() == null ? color(nom) : style.color());
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
  public File apply(EvolutionPatrimoine evolutionPatrimoine, Boolean withAggregates) {
    XYChart chart = new XYChartBuilder().width(800).height(600).build();
    configureStyle(chart);
    configureSeries(evolutionPatrimoine, chart, withAggregates);

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
