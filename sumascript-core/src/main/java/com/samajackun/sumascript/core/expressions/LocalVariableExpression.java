package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class LocalVariableExpression implements Expression
{
	private final String varName;

	public LocalVariableExpression(String varName)
	{
		super();
		this.varName=varName;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		try
		{
			return context.getVariablesManager().getLocalVariable(this.varName);
		}
		catch (VariableNotFoundException e)
		{
			throw new EvaluationException(e);
		}
	}

	@Override
	public String toCode()
	{
		return CodeUtils.toVar(this.varName);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		try
		{
			Object value=context.getVariablesManager().getLocalVariable(this.varName);
			return TypeUtils.guessType(value);
		}
		catch (VariableNotFoundException e)
		{
			throw new MetadataException(e);
		}
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}
}
