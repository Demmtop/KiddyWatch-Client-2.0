package de.demmtop.calendar.view;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.DeleteEvent;
import com.calendarfx.model.Entry;
import com.calendarfx.model.LoadEvent;
import com.calendarfx.model.PopoverEvent;
import com.calendarfx.model.SaveEvent;

import de.demmtop.calendar.pane.EntryPopover;
import de.demmtop.calendar.remote.ICalendarRemote;
import de.demmtop.context.EjbContext;
import de.demmtop.enumeration.ECalendarType;
import de.demmtop.transfer.TransferFactory;
import de.demmtop.transfer.entity.CalendarProperty;
import impl.com.calendarfx.view.util.Util;
import javafx.event.EventHandler;

/**
 * Ableitung der Originalklasse.
 * 
 * @author skinner
 *
 */
public class CalendarView extends com.calendarfx.view.CalendarView
{
	private ICalendarRemote	calendarEjb;
	private final Logger	logger		= LoggerFactory.getLogger(CalendarView.class);
	private boolean			popoverOpen	= false;

	public CalendarView()
	{
		super();
		Util.runInFXThread(() -> {

			try
			{
				calendarEjb = (ICalendarRemote) EjbContext.getEjbContext().lookup("ejb:ear-0.0.1-SNAPSHOT/calendar-ejb-0.0.1-SNAPSHOT/CalendarEJB!de.demmtop.calendar.remote.ICalendarRemote?stateful");
				calendarEjb.setUserName(System.getProperty("user.name"));
			} catch (Exception e)
			{
				logger.error("Create calendar ejb connection", e);
			}

			init();
		});
	}

	private void delete(DeleteEvent evt)
	{
		CalendarProperty<de.demmtop.jpa.entity.Calendar> property = (CalendarProperty<de.demmtop.jpa.entity.Calendar>) evt.getEntry().getUserObject();
		de.demmtop.jpa.entity.Calendar entity = TransferFactory.getEntity(CalendarProperty.class, property);
		calendarEjb.delete(entity);

	}

	/**
	 * Gobale Initialisierung der GUI. Es werden die Kalendar und alle
	 * EventHandler und Callback registriert, damit die GUI an die eigenen
	 * Bedürfnisse angepasst werden kann.
	 */
	private void init()
	{
		Calendar cal;
		getStylesheets().add(this.getClass().getResource("/de/demmtop/calendar/css/calendar.css").toExternalForm());
		CalendarSource myCalendarSource = new CalendarSource("KiddyWatch");

		for (ECalendarType type : ECalendarType.values())
		{
			if (!ECalendarType.UNDEFINED.equals(type))
			{
				cal = new Calendar(type.getName());
				cal.setStyle(type.getStyle());
				myCalendarSource.getCalendars().add(cal);
			}
		}

		myCalendarSource.getCalendars().sort(Comparator.comparing(Calendar::getName));

		getCalendarSources().clear();
		getCalendarSources().addAll(myCalendarSource);
		setEntryFactory(params -> {
			Entry<CalendarProperty<de.demmtop.jpa.entity.Calendar>> entry = new Entry<>(params);
			CalendarProperty<de.demmtop.jpa.entity.Calendar> calProp;
			try
			{
				calProp = TransferFactory.getEntityProperty(CalendarProperty.class, calendarEjb.createEntity(entry.getStartAsLocalDateTime(), entry.getDuration()));
				calProp.bind(entry);
				entry.setUserObject(calProp);
			} catch (NoSuchMethodException e)
			{
				e.printStackTrace();
			}
			return entry;
		});
		setEntryDetailsPopOverContentCallback(params -> new EntryPopover(params, myCalendarSource.getCalendars()));

		setDefaultCalendarProvider(control -> {
			List<? extends Calendar> calendars = myCalendarSource.getCalendars();
			return calendars.stream().filter(calendar -> calendar.getName().equals("Termine")).findFirst().orElse(null);
		});
		getDayPage().getDetailedDayView().getDayView().setEntryViewFactory(entry -> new DayEntryView(entry));
		EventHandler<LoadEvent> handlerLoad = evt -> load(evt);
		addEventHandler(LoadEvent.LOAD, handlerLoad);
		EventHandler<SaveEvent> handlerSave = evt -> save(evt);
		addEventHandler(SaveEvent.SAVE, handlerSave);
		EventHandler<DeleteEvent> handlerDelete = evt -> delete(evt);
		addEventHandler(DeleteEvent.DELETE, handlerDelete);
		addEventHandler(PopoverEvent.OPEN, evt -> popoverOpen = true);
		addEventHandler(PopoverEvent.CLOSE, evt -> popoverOpen = false);
	}

	/**
	 * EventHandler zum Laden der Daten für einen bestimmten Kalendar und
	 * Zeitraum.
	 * 
	 * @param evt
	 *            das {@link LoadEvent} das alle Daten enthält
	 */
	private void load(LoadEvent evt)
	{
		if (!popoverOpen)
		{
			LocalDateTime startDateTime = evt.getStartDate().atTime(evt.getStartTime().toLocalTime());
			LocalDateTime endDateTime = evt.getEndDate().atTime(evt.getEndTime().toLocalTime());
			evt.getCalendarSources().forEach(cs -> cs.getCalendars().parallelStream().forEach(c -> {
				List<de.demmtop.jpa.entity.Calendar> entities = calendarEjb.load(c.getName(), startDateTime, endDateTime);
				entities.parallelStream().forEach(entity -> {
					CalendarProperty<de.demmtop.jpa.entity.Calendar> calProp;
					Entry<CalendarProperty<de.demmtop.jpa.entity.Calendar>> entry = new Entry<>();

					try
					{
						calProp = TransferFactory.getEntityProperty(CalendarProperty.class, entity);
						calProp.setEntityValue(entry);
						calProp.bind(entry);
						entry.setUserObject(calProp);

						if (!c.existEntry(entry))
						{
							c.addEntry(entry);
						}
					} catch (NoSuchMethodException e)
					{
						e.printStackTrace();
					}

				});
			}));
		}
	}

	private void save(SaveEvent evt)
	{
		CalendarProperty<de.demmtop.jpa.entity.Calendar> property = (CalendarProperty<de.demmtop.jpa.entity.Calendar>) evt.getEntry().getUserObject();
		de.demmtop.jpa.entity.Calendar entity = TransferFactory.getEntity(CalendarProperty.class, property);
		calendarEjb.save(entity);
	}

}
