package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class CurrentDirectoryExpression implements Expression
{

	private static final long serialVersionUID = 5835055840601152160L;

	@Override
	public String toCode()
	{
		return CodeUtils.CURRENT_DIR + "()";
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return evaluatorFactory.getRuntimeEvaluator().evaluateCurrentDirectory(context);
	}

	@Override
	public Datatype getDatatype(Context arg0, EvaluatorFactory arg1)
		throws MetadataException
	{
		return Datatype.FILE;
	}

	@Override
	public Expression reduce(EvaluatorFactory arg0)
		throws EvaluationException
	{
		return this;
	}
}
