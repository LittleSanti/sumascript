package com.samajackun.sumascript.core.expressions;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.runtime.Undefined;

public class IndexedExpression implements Expression, Assignable
{
	private static final long serialVersionUID = 8903020730866747689L;

	private final Expression base;

	private final Expression index;

	public IndexedExpression(Expression base, Expression index)
	{
		super();
		this.base=base;
		this.index=index;
	}

	@Override
	public String toCode()
	{
		return this.base.toCode() + "[" + this.index.toCode() + "]";
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object baseValue=this.base.evaluate(context, evaluatorFactory);
		Object indexValue=this.index.evaluate(context, evaluatorFactory);
		Object value;
		if (baseValue instanceof List)
		{
			if (indexValue instanceof Number)
			{
				List<Object> list=(List<Object>)baseValue;
				int p=((Number)indexValue).intValue();
				value=p < list.size()
					? list.get(p)
					: Undefined.getInstance();
			}
			else
			{
				throw new EvaluationException("An integer value is required to index an array, instead of '" + indexValue + "'");
			}
		}
		else if (baseValue instanceof Map)
		{
			value=((Map<Object, Object>)baseValue).get(indexValue);
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
		Object indexValue=this.index.evaluate(context, evaluatorFactory);
		if (baseValue instanceof List)
		{
			if (indexValue instanceof Integer)
			{
				List<Object> list=(List<Object>)baseValue;
				int p=((Number)indexValue).intValue();
				if (p < list.size())
				{
					list.set(p, value);
				}
				else
				{
					list.add(p, value);
				}
			}
			else
			{
				throw new EvaluationException("An integer value is required to index an array, instead of '" + indexValue + "'");
			}
		}
		else if (baseValue instanceof Map)
		{
			((Map<Object, Object>)baseValue).put(indexValue, value);
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
		return new IndexedExpression(this.base.reduce(evaluatorFactory), this.index.reduce(evaluatorFactory));
	}
}
