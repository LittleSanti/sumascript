package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.eval.evaluators.DefaultFunctionEvaluator;
import com.samajackun.rodas.core.model.Expression;

public class SumaFunctionEvaluator extends DefaultFunctionEvaluator implements FunctionEvaluator
{
	public SumaFunctionEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super(evaluatorFactory);
	}

	@Override
	protected Object resolveFunction(Context context, Expression functionExpression)
		throws EvaluationException
	{
		Object obj=super.resolveFunction(context, functionExpression);
		if (obj == null)
		{
			obj=functionExpression.evaluate(context, getEvaluatorFactory());
		}
		return obj;
	}
}
