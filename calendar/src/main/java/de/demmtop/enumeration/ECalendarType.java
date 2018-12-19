package de.demmtop.enumeration;

import com.calendarfx.model.Calendar.Style;

/**
 * Enum für die verschiedenen Arten von Kalendars.
 * 
 * @author skinner
 *
 */
public enum ECalendarType {
		BIRTHDAY("Geburtstage", 102, Style.STYLE5), FAMILIARISATION("Eingewöhnungen", 105, Style.STYLE7), HOLIDAY("Urlaube", 101, Style.STYLE1), JOB("Verabeitungsaufträge", 103, Style.STYLE6), TASK("Aufgaben", 104, Style.STYLE3), TERMIN("Termine", 100, Style.STYLE2), UNDEFINED("", -1, null);

	private short	code;
	private String	name;
	private Style	style;

	private ECalendarType(String name, int code, Style style)
	{
		this.name = name;
		this.code = (short) code;
		this.style = style;
	}

	/**
	 * @return der DB-Wert des Kalenders
	 */
	public short getCode()
	{
		return code;
	}

	/**
	 * @return der Name des Kalenders
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return der CSS-Style des Kalenders
	 */
	public Style getStyle()
	{
		return style;
	}

}
