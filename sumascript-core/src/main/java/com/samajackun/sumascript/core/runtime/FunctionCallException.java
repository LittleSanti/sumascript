package com.samajackun.sumascript.core.runtime;

import com.samajackun.rodas.core.eval.EvaluationException;

public class FunctionCallException extends EvaluationException
{
	private static final long serialVersionUID=4922692800823365947L;

	public FunctionCallException(String message)
	{
		super(message);
	}

	public FunctionCallException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FunctionCallException(Throwable cause)
	{
		super(cause);
	}
}
