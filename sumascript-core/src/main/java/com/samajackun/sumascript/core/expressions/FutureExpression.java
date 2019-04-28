package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class FutureExpression implements Expression
{
	private final Expression targetExpression;

	public FutureExpression(Expression targetExpression)
	{
		super();
		this.targetExpression=targetExpression;
	}

	@Override
	public String toCode()
	{
		return "future(" + this.targetExpression.toCode() + ")";
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.targetExpression.evaluate(context, evaluatorFactory);
	}

	@Override
	public Datatype getDatatype(Context arg0, EvaluatorFactory arg1)
		throws MetadataException
	{
		return Datatype.UNKNOWN;
	}

	@Override
	public Expression reduce(EvaluatorFactory arg0)
		throws EvaluationException
	{
		return this;
	}
}
