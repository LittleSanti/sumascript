package com.samajackun.sumascript.core;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Namespace
{
	private String serial;

	private final List<String> steps;

	public Namespace(List<String> steps)
	{
		super();
		this.steps=steps;
		String serial="";
		for (String step : steps)
		{
			if (serial.length() > 0)
			{
				serial+=".";
			}
			serial+=step;
		}
	}

	public Namespace(String serial)
	{
		this(tokenize(serial));
	}

	private static List<String> tokenize(String serial)
	{
		StringTokenizer stk=new StringTokenizer(serial, ".");
		List<String> list=new ArrayList<>(stk.countTokens());
		for (int i=0; i < stk.countTokens(); i++)
		{
			list.add(stk.nextToken());
		}
		return list;
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.steps == null)
			? 0
			: this.steps.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		Namespace other=(Namespace)obj;
		if (this.steps == null)
		{
			if (other.steps != null)
			{
				return false;
			}
		}
		else if (!this.steps.equals(other.steps))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return this.serial;
	}

}
