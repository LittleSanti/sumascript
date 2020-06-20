package com.samajackun.sumascript.core.expressions;

import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

// Representa un derreferenciación: container.property
public class ScopedExpression implements Expression, Assignable
{
	private static final long serialVersionUID = -1709353728137908653L;

	private final Expression base;

	private final String property;

	public ScopedExpression(Expression base, String property)
	{
		super();
		this.base=base;
		this.property=property;
	}

	@Override
	public String toCode()
	{
		return this.base.toCode() + "[" + this.property + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object baseValue=this.base.evaluate(context, evaluatorFactory);
		Object value;
		if (baseValue instanceof Map)
		{
			value=((Map<Object, Object>)baseValue).get(this.property);
		}
		else
		{
			throw new EvaluationException("An array or map is required to be indexed, instead of '" + baseValue + "'");
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(Context context, EvaluatorFactory evaluatorFactory, Object value)
		throws EvaluationException
	{
		Object baseValue=this.base.evaluate(context, evaluatorFactory);
		if (baseValue instanceof Map)
		{
			((Map<Object, Object>)baseValue).put(this.property, value);
		}
		else
		{
			throw new EvaluationException("An array or map is required to be indexed, instead of '" + baseValue + "'");
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
		return new ScopedExpression(this.base.reduce(evaluatorFactory), this.property);
	}
}
