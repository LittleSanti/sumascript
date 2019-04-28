package com.samajackun.sumascript.core.expressions;

import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class ReferencedExpression implements Expression, Assignable
{
	private final Expression base;

	private final Expression index;

	public ReferencedExpression(Expression base, Expression index)
	{
		super();
		this.base=base;
		this.index=index;
	}

	@Override
	public String toCode()
	{
		return this.base.toCode() + "." + this.index.toCode();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object baseValue=this.base.evaluate(context, evaluatorFactory);
		Object indexValue=this.index.evaluate(context, evaluatorFactory);
		Object value;
		if (baseValue instanceof Map)
		{
			value=((Map<Object, Object>)baseValue).get(indexValue);
		}
		else
		{
			throw new EvaluationException("A map is required to be indexed, instead of '" + baseValue + "'");
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Context context, EvaluatorFactory evaluatorFactory, Object value)
		throws EvaluationException
	{
		Object baseValue=this.base.evaluate(context, evaluatorFactory);
		Object indexValue=this.index.getName();
		if (baseValue instanceof Map)
		{
			((Map<Object, Object>)baseValue).put(indexValue, value);
		}
		else
		{
			throw new EvaluationException("A map is required to be indexed, instead of '" + baseValue + "'");
		}
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return this.base.getDatatype(context, evaluatorFactory);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return new ReferencedExpression(this.base.reduce(evaluatorFactory), this.index.reduce(evaluatorFactory));
	}
}
