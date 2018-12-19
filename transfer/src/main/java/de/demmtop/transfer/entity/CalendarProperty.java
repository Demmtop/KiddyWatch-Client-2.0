package de.demmtop.transfer.entity;

import java.time.LocalDateTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanObjectProperty;
import javafx.beans.property.adapter.JavaBeanObjectPropertyBuilder;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;

public class CalendarProperty<E> implements ITransferEntity<E>
{
	private StringProperty							infoProperty;
	private StringProperty							calendarName;
	private BooleanProperty							fullDayProperty;
	private StringProperty							recurrenceRuleProperty;
	private StringProperty							titleProperty;
	private JavaBeanObjectProperty<LocalDateTime>	startDateTimeProperty;
	private JavaBeanObjectProperty<LocalDateTime>	endDateTimeProperty;
	private ObjectProperty<Interval>				intervalProperty;
	private ObjectProperty<Calendar>				calendarProperty;
	private StringProperty							idProperty;
	private StringProperty							entryUserProperty;
	private E										entity;

	public CalendarProperty(E entity) throws NoSuchMethodException
	{
		this.entity = entity;
		infoProperty = new JavaBeanStringPropertyBuilder().bean(entity).name("info").build();
		calendarName = new JavaBeanStringPropertyBuilder().bean(entity).name("calendarName").build();
		fullDayProperty = new JavaBeanBooleanPropertyBuilder().bean(entity).name("fullDay").build();
		recurrenceRuleProperty = new JavaBeanStringPropertyBuilder().bean(entity).name("recurrenceRule").build();
		titleProperty = new JavaBeanStringPropertyBuilder().bean(entity).name("title").build();
		startDateTimeProperty = new JavaBeanObjectPropertyBuilder<LocalDateTime>().bean(entity).name("startDateTime").build();
		endDateTimeProperty = new JavaBeanObjectPropertyBuilder<LocalDateTime>().bean(entity).name("endDateTime").build();
		intervalProperty = new SimpleObjectProperty<>();
		intervalProperty.set(new Interval(startDateTimeProperty.get(), endDateTimeProperty.get()));
		intervalProperty.addListener((observable, oldValue, newValue) -> {
			startDateTimeProperty.set(newValue.getStartDateTime());
			endDateTimeProperty.set(newValue.getEndDateTime());
		});
		calendarProperty = new SimpleObjectProperty<>();
		calendarProperty.addListener((observable, oldValue, newValue) -> {
			calendarName.set(newValue == null ? null : newValue.getName());
		});
		idProperty = new JavaBeanStringPropertyBuilder().bean(entity).name("id").build();
		entryUserProperty = new JavaBeanStringPropertyBuilder().bean(entity).name("entryUser").build();
	}

	public void bind(Entry<CalendarProperty<E>> entry)
	{
		Bindings.bindBidirectional(titleProperty, entry.titleProperty());
		Bindings.bindBidirectional(fullDayProperty, entry.fullDayProperty());
		Bindings.bindBidirectional(recurrenceRuleProperty, entry.recurrenceRuleProperty());
		Bindings.bindBidirectional(intervalProperty, entry.intervalProperty());
		Bindings.bindBidirectional(calendarProperty, entry.calendarProperty());
		Bindings.bindBidirectional(infoProperty, entry.infoProperty());
	}

	@Override
	public E getEntity()
	{
		return entity;
	}

	public void setEntityValue(Entry<CalendarProperty<E>> entry)
	{
		entry.titleProperty().set(titleProperty.get());
		entry.infoProperty().set(infoProperty.get());
		entry.calendarProperty().set(calendarProperty.get());
		entry.fullDayProperty().set(fullDayProperty.get());
		entry.recurrenceRuleProperty().set(recurrenceRuleProperty.get());
		entry.intervalProperty().set(intervalProperty.get());
		entry.entryUserProperty().set(entryUserProperty.get());
		entry.setId(idProperty.get());
		entry.readOnlyProperty().set("SYSTEM".equals(entryUserProperty.get()));
	}
}
