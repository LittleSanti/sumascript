package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.EvaluationException;

public class NotAFunctionException extends EvaluationException
{
	private static final long serialVersionUID=509717852158357079L;

	public NotAFunctionException()
	{
		super("Object is not a function");
	}
}
