package com.samajackun.sumascript.core.runtime;

import com.samajackun.rodas.core.eval.AbstractVariablesManager;

public class FlexibleVariablesManager extends AbstractVariablesManager
{
	@Override
	protected FlexibleVariablesContext createVariablesContext()
	{
		return new FlexibleVariablesContext();
	}
}
