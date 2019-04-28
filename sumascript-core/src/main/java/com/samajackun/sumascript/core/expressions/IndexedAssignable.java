package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class IndexedAssignable implements Expression, Assignable
{
	// private final Assignable base;

	@Override
	public String toCode()
	{
		return null;
	}

	@Override
	public void set(Context context, EvaluatorFactory evaluatorFactory, Object value)
		throws EvaluationException
	{
		// TODO Auto-generated method stub

	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		// TODO Auto-generated method stub
		return null;
	}

}
