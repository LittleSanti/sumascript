package com.samajackun.sumascript.core.expressions;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.VariableNotFoundException;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class NearestVariableExpression implements Expression, Assignable
{
	private static final long serialVersionUID=-7224765905160309662L;

	private final Name varName;

	private final List<Expression> subexpressions=new ArrayList<>();

	public NearestVariableExpression(Name varName)
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
			return context.getVariablesManager().getNearestVariable(this.varName);
		}
		catch (VariableNotFoundException e)
		{
			throw new EvaluationException(e);
		}
	}

	@Override
	public void set(Context context, EvaluatorFactory evaluatorFactory, Object value)
		throws EvaluationException
	{
		context.getVariablesManager().setNearestVariable(this.varName, value);
	}

	@Override
	public String toCode()
	{
		return this.varName.asString();
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		try
		{
			Object value=context.getVariablesManager().getNearestVariable(this.varName);
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

	@Override
	public List<Expression> getSubExpressions()
	{
		return this.subexpressions;
	}

	public void addSubexpression(Expression subexpression)
	{
		this.subexpressions.add(subexpression);
	}
}
