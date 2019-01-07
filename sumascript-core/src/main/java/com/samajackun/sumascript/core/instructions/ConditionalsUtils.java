package com.samajackun.sumascript.core.instructions;

final class ConditionalsUtils
{
	private ConditionalsUtils()
	{
	}

	public static boolean isTrue(Object value)
	{
		return value != null && value != Boolean.FALSE && !value.toString().isEmpty() && !(value instanceof Number && ((Number)value).intValue() == 0);
	}

	public static boolean isFalse(Object value)
	{
		return value == null || value == Boolean.FALSE || value.toString().isEmpty() || value instanceof Number && ((Number)value).intValue() == 0;
	}
}
