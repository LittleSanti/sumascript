package com.samajackun.sumascript.core.runtime;

import com.samajackun.rodas.core.eval.AbstractVariablesManager;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariablesContext;

public class FlexibleVariablesManager extends AbstractVariablesManager
{
	public FlexibleVariablesManager(VariablesContext globalVariables)
	{
		super(globalVariables);
	}

	@Override
	protected Object getValueForVariableNotFound(Name name)
	{
		return Undefined.getInstance();
	}
}
