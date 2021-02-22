package com.samajackun.sumascript.core.expressions;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

import com.samajackun.rodas.core.model.Datatype;

public final class TypeUtils
{
	public static Datatype guessType(Object value)
	{
		Datatype datatype;
		if (value == null)
		{
			datatype=Datatype.NULL;
		}
		else if (value instanceof String)
		{
			datatype=Datatype.TEXT;
		}
		else if (value instanceof Double || value instanceof Float || value instanceof BigDecimal)
		{
			datatype=Datatype.DECIMAL_NUMBER;
		}
		else if (value instanceof Number)
		{
			datatype=Datatype.INTEGER_NUMBER;
		}
		else if (value instanceof File)
		{
			datatype=Datatype.FILE;
		}
		else if (value instanceof Boolean)
		{
			datatype=Datatype.BOOLEAN;
		}
		else if (value instanceof Date)
		{
			datatype=Datatype.DATETIME;
		}
		// TODO A lo mehor faltaría el tipo DATE.
		else
		{
			datatype=Datatype.OBJECT;
		}
		return datatype;
	}
}
