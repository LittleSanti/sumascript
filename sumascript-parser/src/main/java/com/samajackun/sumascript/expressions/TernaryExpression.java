package com.samajackun.sumascript.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.instructions.ConditionalsUtils;

public class TernaryExpression implements Expression
{
	private final Expression conditional;

	private final Expression direct;

	private final Expression inverse;

	public TernaryExpression(Expression conditional, Expression direct, Expression inverse)
	{
		super();
		this.conditional=conditional;
		this.direct=direct;
		this.inverse=inverse;
	}

	public Expression getConditional()
	{
		return conditional;
	}

	public Expression getDirect()
	{
		return direct;
	}

	public Expression getInverse()
	{
		return inverse;
	}

	@Override
	public String toCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object conditionalValue=conditional.evaluate(context, evaluatorFactory);
		Object result=ConditionalsUtils.isTrue(conditionalValue)
			? direct.evaluate(context, evaluatorFactory)
			: inverse.evaluate(context, evaluatorFactory);
		return result;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reducedDirect=direct.reduce(evaluatorFactory);
		Expression reducedInverse=inverse.reduce(evaluatorFactory);
		Expression reducedConditional=conditional.reduce(evaluatorFactory);
		if (reducedConditional instanceof ConstantExpression)
		{
			Object conditionalValue=((ConstantExpression)reducedConditional).getValue();
			reduced=ConditionalsUtils.isTrue(conditionalValue)
				? reducedDirect
				: reducedInverse;
		}
		else
		{
			reduced=new TernaryExpression(reducedConditional, reducedDirect, reducedInverse);
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		Datatype d1=direct.getDatatype(context, evaluatorFactory);
		Datatype d2=inverse.getDatatype(context, evaluatorFactory);
		return d1 == d2
			? d1
			: Datatype.UNKNOWN;
	}
}
