package de.demmtop.transfer;

import de.demmtop.transfer.entity.CalendarProperty;
import de.demmtop.transfer.entity.ITransferEntity;

public class TransferFactory
{
	public static <T, E> E getEntity(Class<T> propertyType, T property)
	{
		return ((ITransferEntity<E>) propertyType.cast(property)).getEntity();
	}

	public static <T, E> T getEntityProperty(Class<T> propertyType, E entity) throws NoSuchMethodException
	{
		if (propertyType == de.demmtop.transfer.entity.CalendarProperty.class)
		{
			return (T) new CalendarProperty<>(entity);
		}

		return null;
	}
}
