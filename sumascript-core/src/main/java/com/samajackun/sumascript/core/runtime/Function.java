package com.samajackun.sumascript.core.runtime;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;

public interface Function
{
	public Object call(Context context, List<Object> argumentValues)
		throws FunctionCallException;
}
