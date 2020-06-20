package com.samajackun.sumascript.core.runtime;

import java.util.function.Supplier;

import com.samajackun.rodas.core.eval.AbstractVariablesContext;
import com.samajackun.rodas.core.eval.Name;

public class FlexibleVariablesContext extends AbstractVariablesContext
{
	@Override
	public Object get(Name name)
	{
		Object value;
		Supplier<Object> supplier=getMap().get(name);
		if (supplier != null)
		{
			value=supplier.get();
			if (value == null)
			{
				if (!getMap().containsKey(name))
				{
					value=Undefined.getInstance();
				}
			}
		}
		else
		{
			value=Undefined.getInstance();
		}
		return value;
	}
}
