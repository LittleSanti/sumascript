package com.samajackun.sumascript.core.runtime;

import com.samajackun.rodas.core.eval.AbstractVariablesContext;
import com.samajackun.rodas.core.eval.Name;

public class FlexibleVariablesContext extends AbstractVariablesContext
{
	@Override
	public Object get(Name name)
	{
		Object value=getMap().get(name);
		if (value == null)
		{
			if (!getMap().containsKey(name))
			{
				value=Undefined.getInstance();
			}
		}
		return value;
	}
}
