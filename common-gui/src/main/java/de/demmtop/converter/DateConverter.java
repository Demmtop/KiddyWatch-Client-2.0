package de.demmtop.converter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateConverter
{
	public static LocalDate convertDateToLocalDate(Date date)
	{
		if (date != null)
		{
			Instant instant = Instant.ofEpochMilli(date.getTime());
			return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		}
		else
		{
			return null;
		}
	}

	public static LocalDateTime convertDateToLocalDateTime(Date date)
	{
		if (date != null)
		{
			Instant instant = Instant.ofEpochMilli(date.getTime());
			return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
		}
		else
		{
			return null;
		}
	}

	public static Date convertLocalDateTimeToDate(LocalDateTime date)
	{
		if (date != null)
		{
			Instant instant = date.atZone(ZoneId.systemDefault()).toInstant();
			return Date.from(instant);
		}
		else
		{
			return null;
		}
	}

	public static Date convertLocalDateToDate(LocalDate date)
	{
		if (date != null)
		{
			Instant instant = date.atTime(LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant();
			return Date.from(instant);
		}
		else
		{
			return null;
		}
	}
}
