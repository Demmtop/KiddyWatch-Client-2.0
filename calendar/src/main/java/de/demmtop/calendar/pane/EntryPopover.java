package de.demmtop.calendar.pane;

import java.io.IOException;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.PopoverEvent;
import com.calendarfx.model.SaveEvent;
import com.calendarfx.view.DateControl.EntryDetailsPopOverContentParameter;

import de.demmtop.calendar.controller.EntryPopoverController;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class EntryPopover extends Parent
{

	public EntryPopover(EntryDetailsPopOverContentParameter params, ObservableList<Calendar> calendars)
	{
		super();
		try
		{
			FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/de/demmtop/fxml/pane/calendar/EntryPopover.fxml"));
			Node node = loader.load();
			((EntryPopoverController) loader.getController()).setCalendarEntry(params.getEntry(), calendars);
			getChildren().add(node);
			params.getPopOver().setOnHidden(evt -> {
				params.getDateControl().fireEvent(new PopoverEvent(PopoverEvent.CLOSE, params.getEntry()));

				if (!params.getEntry().readOnlyProperty().get())
				{
					params.getDateControl().fireEvent(new SaveEvent(SaveEvent.SAVE, params.getEntry()));
				}
			});
			params.getPopOver().setOnShowing(evt -> {
				params.getDateControl().fireEvent(new PopoverEvent(PopoverEvent.OPEN, params.getEntry()));
			});
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
