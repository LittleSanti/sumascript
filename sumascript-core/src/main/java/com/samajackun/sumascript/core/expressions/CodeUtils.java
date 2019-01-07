package com.samajackun.sumascript.core.expressions;

import java.util.List;

import com.samajackun.rodas.core.model.Expression;

final class CodeUtils
{
	public static final String CURRENT_DIR="dir";

	private CodeUtils()
	{
	}

	public static String toVar(String varName)
	{
		return "${" + varName + "}";
	}

	public static String serialize(List<Expression> arguments)
	{
		String serial="";
		for (Expression argument : arguments)
		{
			if (!serial.isEmpty())
			{
				serial+=",";
			}
			serial+=argument.toCode();
		}
		return serial;
	}
}
