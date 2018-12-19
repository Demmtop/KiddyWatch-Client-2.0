package de.demmtop.calendar.controller;

import java.time.format.FormatStyle;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarSelector;
import com.calendarfx.view.RecurrenceView;
import com.calendarfx.view.popover.RecurrencePopup;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.util.converter.LocalTimeStringConverter;

public class EntryPopoverController
{
	@FXML
	private TextField			entryTitle;
	@FXML
	private CheckBox			entryFullDay;
	@FXML
	private TextArea			entryInfo;
	@FXML
	private JFXDatePicker		entryFromDate;
	@FXML
	private JFXDatePicker		entryToDate;
	@FXML
	private JFXTimePicker		entryFromTime;
	@FXML
	private JFXTimePicker		entryToTime;
	@FXML
	private CalendarSelector	calendarSelector;
	@FXML
	private MenuButton			recurrenceButton;
	@FXML
	private TitledPane			paneInfo;

	/**
	 * Initialisiert das Entry Popup mit den Werten.
	 * 
	 * @param entry
	 *            das Kalendar-Objekt
	 * @param calendars
	 *            Liste aller verfügbaren Kalendars
	 */
	public void setCalendarEntry(Entry<?> entry, ObservableList<Calendar> calendars)
	{
		Bindings.bindBidirectional(entryTitle.textProperty(), entry.titleProperty());
		Bindings.bindBidirectional(entryInfo.textProperty(), entry.infoProperty());
		Bindings.bindBidirectional(calendarSelector.calendarProperty(), entry.calendarProperty());
		Bindings.bindBidirectional(entryFullDay.selectedProperty(), entry.fullDayProperty());

		calendarSelector.getCalendars().setAll(calendars);
		calendarSelector.setCalendar(entry.getCalendar());
		entryFullDay.setOnAction(evt -> {
			entry.setFullDay(entryFullDay.isSelected());

			// if (entryFullDay.isSelected())
			// {
			// entryFromTime.setValue(LocalTime.MIN);
			// entryToTime.setValue(LocalTime.MAX);
			// } else
			// {
			// entryFromTime.setValue(LocalTime.now());
			// entryToTime.setValue(entryFromTime.getValue().plusMinutes(30));
			// entryToDate.setValue(entryFromDate.getValue());
			// }
		});

		if (StringUtils.isEmpty(entryInfo.textProperty().getValue()))
		{
			paneInfo.expandedProperty().set(false);
		} else
		{
			paneInfo.expandedProperty().set(true);
		}

		setDateValues(entry);
		registerListener(entry);
		setStyleClass(entry);
		setDisableProperty(entry);
		createRecurrenceButton(entry);

		updateRecurrenceRuleButton(entry);

		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				calendarSelector.requestFocus();
			}
		});
	}

	/**
	 * Erstellt den Menübutton für den Seriendialog.
	 * 
	 * @param entry
	 *            der aktulle Termin
	 */
	private void createRecurrenceButton(Entry<?> entry)
	{
		MenuItem none = new MenuItem("Ohne");
		MenuItem everyDay = new MenuItem("Jeden Tag");
		MenuItem everyWeek = new MenuItem("Jede Woche");
		MenuItem everyMonth = new MenuItem("Jeden Monat");
		MenuItem everyYear = new MenuItem("Jedes Jahr");
		MenuItem custom = new MenuItem("Eigene...");

		none.setOnAction(evt -> updateRecurrenceRule(entry, null));
		everyDay.setOnAction(evt -> updateRecurrenceRule(entry, "RRULE:FREQ=DAILY"));
		everyWeek.setOnAction(evt -> updateRecurrenceRule(entry, "RRULE:FREQ=WEEKLY"));
		everyMonth.setOnAction(evt -> updateRecurrenceRule(entry, "RRULE:FREQ=MONTHLY"));
		everyYear.setOnAction(evt -> updateRecurrenceRule(entry, "RRULE:FREQ=YEARLY"));
		custom.setOnAction(evt -> showRecurrenceEditor(entry));

		recurrenceButton.getItems().setAll(none, everyDay, everyWeek, everyMonth, everyYear, new SeparatorMenuItem(), custom);
	}

	@FXML
	private void initialize()
	{
		entryFromTime.set24HourView(true);
		entryFromTime.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.GERMAN));
		entryToTime.set24HourView(true);
		entryToTime.setConverter(new LocalTimeStringConverter(FormatStyle.SHORT, Locale.GERMAN));
	}

	/**
	 * Registriert Listener an den verschiedenen Properties.
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 */
	private void registerListener(Entry<?> entry)
	{
		entry.recurrenceRuleProperty().addListener(it -> updateRecurrenceRuleButton(entry));
		entryFromDate.valueProperty().addListener(evt -> entry.changeStartDate(entryFromDate.getValue(), true));
		entryFromTime.valueProperty().addListener(evt -> entry.changeStartTime(entryFromTime.getValue(), true));
		entryToDate.valueProperty().addListener(evt -> entry.changeEndDate(entryToDate.getValue(), false));
		entryToTime.valueProperty().addListener(evt -> entry.changeEndTime(entryToTime.getValue(), false));

		entry.intervalProperty().addListener(it -> {
			entryFromTime.setValue(entry.getStartTime());
			entryToTime.setValue(entry.getEndTime());
			entryFromDate.setValue(entry.getStartDate());
			entryToDate.setValue(entry.getEndDate());
		});

		entry.calendarProperty().addListener((observable, oldCalendar, newCalendar) -> {
			if (oldCalendar != null)
			{
				entryTitle.getStyleClass().remove(oldCalendar.getStyle() + "-entry-popover-title");
				entryFromDate.getStyleClass().remove(oldCalendar.getStyle() + "-jfx-date-picker");
				entryFromTime.getStyleClass().remove(oldCalendar.getStyle() + "-jfx-date-picker");
				entryToDate.getStyleClass().remove(oldCalendar.getStyle() + "-jfx-date-picker");
				entryToTime.getStyleClass().remove(oldCalendar.getStyle() + "-jfx-date-picker");
			}
			if (newCalendar != null)
			{
				entryTitle.getStyleClass().add(newCalendar.getStyle() + "-entry-popover-title");
				entryFromDate.getStyleClass().add(newCalendar.getStyle() + "-jfx-date-picker");
				entryFromTime.getStyleClass().add(newCalendar.getStyle() + "-jfx-date-picker");
				entryToDate.getStyleClass().add(newCalendar.getStyle() + "-jfx-date-picker");
				entryToTime.getStyleClass().add(newCalendar.getStyle() + "-jfx-date-picker");
			}
		});
	}

	/**
	 * Setzt das Von-Bis Datum/Uhrzeit in die Felder
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 */
	private void setDateValues(Entry<?> entry)
	{
		entryFromDate.setValue(entry.getStartDate());
		entryFromTime.setValue(entry.getStartTime());
		entryToDate.setValue(entry.getEndDate());
		entryToTime.setValue(entry.getEndTime());
	}

	/**
	 * Setzt das Editable Flag an einigen Feldern.
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 */
	private void setDisableProperty(Entry<?> entry)
	{
		calendarSelector.disableProperty().bind(entry.readOnlyProperty());
		entryInfo.editableProperty().bind(entry.readOnlyProperty().not());
		entryFromDate.disableProperty().bind(entry.readOnlyProperty());
		entryFromTime.disableProperty().bind(entry.readOnlyProperty().or(entry.fullDayProperty()));
		entryToDate.disableProperty().bind(entry.readOnlyProperty());
		entryToTime.disableProperty().bind(entry.readOnlyProperty().or(entry.fullDayProperty()));
		entryFullDay.disableProperty().bind(entry.readOnlyProperty());
		recurrenceButton.disableProperty().bind(entry.readOnlyProperty());
		entryTitle.disableProperty().bind(entry.readOnlyProperty());
	}

	/**
	 * Setzt die Stylesheet Klassen an die Felder.
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 */
	private void setStyleClass(Entry<?> entry)
	{
		entryTitle.getStyleClass().add(entry.getCalendar().getStyle() + "-entry-popover-title");
		entryFromDate.getStyleClass().add(entry.getCalendar().getStyle() + "-jfx-date-picker");
		entryFromTime.getStyleClass().add(entry.getCalendar().getStyle() + "-jfx-date-picker");
		entryToDate.getStyleClass().add(entry.getCalendar().getStyle() + "-jfx-date-picker");
		entryToTime.getStyleClass().add(entry.getCalendar().getStyle() + "-jfx-date-picker");
	}

	/**
	 * Öffnet den Dialog für das Erstellen eines eingen Serientermins.
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 */
	private void showRecurrenceEditor(Entry<?> entry)
	{
		RecurrencePopup popup = new RecurrencePopup();
		RecurrenceView recurrenceView = popup.getRecurrenceView();
		String recurrenceRule = entry.getRecurrenceRule();
		if (recurrenceRule == null || recurrenceRule.trim().equals(""))
		{
			recurrenceRule = "RRULE:FREQ=DAILY;";
		}
		recurrenceView.setRecurrenceRule(recurrenceRule);
		popup.setOnOkPressed(evt -> {
			String rrule = recurrenceView.getRecurrenceRule();
			entry.setRecurrenceRule(rrule);
		});

		Point2D anchor = recurrenceButton.localToScreen(0, recurrenceButton.getHeight());
		popup.show(recurrenceButton, anchor.getX(), anchor.getY());
	}

	/**
	 * Setzt die aktuelle Serien Regel in den Termin.
	 * 
	 * @param entry
	 *            der aktuelle Termin
	 * @param rule
	 *            die Serien Regel
	 */
	private void updateRecurrenceRule(Entry<?> entry, String rule)
	{
		entry.setRecurrenceRule(rule);
	}

	/**
	 * Setzt den aktullen Wert der Serien Regel auf den Button.
	 * 
	 * @param entry
	 *            der aktulle Termin
	 */
	private void updateRecurrenceRuleButton(Entry<?> entry)
	{
		String rule = entry.getRecurrenceRule();
		if (rule == null)
		{
			recurrenceButton.setText("Ohne");
		} else
		{
			switch (rule.trim().toUpperCase())
			{
				case "RRULE:FREQ=DAILY" :
					recurrenceButton.setText("Jeden Tag");
					break;
				case "RRULE:FREQ=WEEKLY" :
					recurrenceButton.setText("Jede Woche");
					break;
				case "RRULE:FREQ=MONTHLY" :
					recurrenceButton.setText("Jeden Monat");
					break;
				case "RRULE:FREQ=YEARLY" :
					recurrenceButton.setText("Jedes Jahr");
					break;
				default :
					recurrenceButton.setText("Eigene...");
					break;
			}
		}
	}
}
