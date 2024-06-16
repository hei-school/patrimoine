package school.hei.patrimoine.visualisation.swing.ihm;

import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.swing.ihm.jdatepicker.DateFormatter;
import school.hei.patrimoine.visualisation.swing.modele.EvolutionPatrimoineObservable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

public class SpecifieurPatrimoineIHM extends JPanel implements Observer {
  private final EvolutionPatrimoineObservable evolutionPatrimoineObservable;

  public SpecifieurPatrimoineIHM(EvolutionPatrimoineObservable evolutionPatrimoineObservable) {
    super();
    this.evolutionPatrimoineObservable = evolutionPatrimoineObservable;
    this.evolutionPatrimoineObservable.addObserver(this);

    configureStyle();
    configurePeriodeEvolution();
  }

  private void configurePeriodeEvolution() {
    var ancienneEvolution = evolutionPatrimoineObservable.getEvolutionPatrimoine();
    this.add(((Component) datePicker(
        "De",
        ancienneEvolution.getDebut(),
        e -> evolutionPatrimoineObservable.setEvolutionPatrimoine(
            evolutionPatrimoine() //note(fresh-evolution-in-lambda): MUST use function to have fresh copy inside lambda
                .nouveauDebut(toLocalDate(e))))));
    this.add(((Component) datePicker(
        "À",
        ancienneEvolution.getFin(),
        e -> evolutionPatrimoineObservable.setEvolutionPatrimoine(
            evolutionPatrimoine() //note(fresh-evolution-in-lambda)
                .nouvelleFin(toLocalDate(e))))));
  }

  private EvolutionPatrimoine evolutionPatrimoine() {
    return evolutionPatrimoineObservable.getEvolutionPatrimoine();
  }

  private LocalDate toLocalDate(ActionEvent e) {
    return toLocalDate(((JDatePanelImpl) e.getSource()).getModel());
  }

  private LocalDate toLocalDate(DateModel<?> model) {
    return LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
  }

  private void configureStyle() {
    var size = new Dimension(500, 500);
    this.setSize(size);
    this.setMinimumSize(size);
    this.setPreferredSize(size);
  }

  private JDatePicker datePicker(String label, LocalDate parDefaut, ActionListener actionListener) {
    this.add(new JLabel(label));

    var i18n = new Properties();
    i18n.put("text.today", "ce jour");
    var datePanel = new JDatePanelImpl(
        new UtilDateModel(Date.from(parDefaut.atStartOfDay(ZoneId.systemDefault()).toInstant())),
        i18n);
    var datePicker = new JDatePickerImpl(datePanel, new DateFormatter());
    datePicker.addActionListener(actionListener);
    this.add(datePicker);

    return datePicker;
  }

  @Override
  public void update(Observable o, Object arg) {
    this.repaint();
  }
}
