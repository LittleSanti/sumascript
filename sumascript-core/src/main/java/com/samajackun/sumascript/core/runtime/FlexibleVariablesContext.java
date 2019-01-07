package com.samajackun.sumascript.core.runtime;

import com.samajackun.rodas.core.eval.AbstractVariablesContext;

public class FlexibleVariablesContext extends AbstractVariablesContext
{
	@Override
	public Object get(String name)
	{
		Object value=getMap().get(name);
		return value != null
			? value
			: getMap().containsKey(name)
				? null
				: Undefined.getInstance();
	}
}
