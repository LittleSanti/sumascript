package com.samajackun.sumascript.expressions;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.runtime.Undefined;

public final class UndefinedConstantExpression implements Expression
{
	private static final long serialVersionUID=-13317900642158244L;

	private static final UndefinedConstantExpression INSTANCE=new UndefinedConstantExpression();

	public static UndefinedConstantExpression getInstance()
	{
		return INSTANCE;
	}

	private UndefinedConstantExpression()
	{
	}

	@Override
	public String toCode()
	{
		return "undefined";
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return Undefined.getInstance();
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.UNDEFINED;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.emptyList();
	}

	@Override
	public boolean equals(Object obj)
	{
		return obj == this;
	}
}
