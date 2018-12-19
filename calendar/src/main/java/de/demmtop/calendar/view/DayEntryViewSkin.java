package de.demmtop.calendar.view;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DayEntryView;
import com.calendarfx.view.DraggedEntry;

import javafx.scene.control.Label;

public class DayEntryViewSkin extends impl.com.calendarfx.view.DayEntryViewSkin
{
	protected Label recurrence;

	public DayEntryViewSkin(DayEntryView view)
	{
		super(view);

		recurrence = createRecurrenceLabel();
		recurrence.setManaged(false);
		recurrence.setMouseTransparent(true);

		getChildren().add(recurrence);
	}

	/**
	 * @returns Das Label das das Icon für eine Serie anzeigt.
	 */
	protected Label createRecurrenceLabel()
	{
		Label label = new Label();
		label.setMinSize(0, 0);

		return label;
	}

	@Override
	protected void updateLabels()
	{
		super.updateLabels();
		Entry<?> entry = getEntry();
		Calendar calendar = entry.getCalendar();

		if (entry instanceof DraggedEntry)
		{
			calendar = ((DraggedEntry) entry).getOriginalCalendar();
		}

		if (entry.isRecurrence())
		{
			recurrence.getStyleClass().add(calendar.getStyle() + "-entry-recurrence-label");
		}
	}

}
