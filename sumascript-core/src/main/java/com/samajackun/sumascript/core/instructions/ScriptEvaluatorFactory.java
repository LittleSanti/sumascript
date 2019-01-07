package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;

public final class ScriptEvaluatorFactory extends DefaultEvaluatorFactory
{
	private static final ScriptEvaluatorFactory INSTANCE=new ScriptEvaluatorFactory();

	public static ScriptEvaluatorFactory getInstance()
	{
		return INSTANCE;
	}

	private ScriptEvaluatorFactory()
	{
	}

}
