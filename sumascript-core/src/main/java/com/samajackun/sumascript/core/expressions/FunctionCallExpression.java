package com.samajackun.sumascript.core.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.runtime.Function;

public class FunctionCallExpression implements Expression
{
	private final Expression functionObject;

	private final List<Expression> arguments;

	public FunctionCallExpression(Expression functionObject, List<Expression> arguments)
	{
		super();
		this.functionObject=functionObject;
		this.arguments=arguments;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object obj=this.functionObject.evaluate(context, evaluatorFactory);
		if (!(obj instanceof Function))
		{
			throw new NotAFunctionException();
		}
		Function function=(Function)obj;
		List<Object> argumentValues=evaluateArguments(context, evaluatorFactory);
		Object returnValue=function.call(context, argumentValues);
		return returnValue;
	}

	private List<Object> evaluateArguments(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		List<Object> values=new ArrayList<>(this.arguments.size());
		for (Expression argument : this.arguments)
		{
			Object value=argument.evaluate(context, evaluatorFactory);
			values.add(value);
		}
		return values;
	}

	@Override
	public String toCode()
	{
		return this.functionObject.toCode() + "(" + CodeUtils.serialize(this.arguments) + ")";
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return this.functionObject.getDatatype(context, evaluatorFactory);
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reducedExpression;
		List<Expression> reducedArguments=new ArrayList<>(this.arguments.size());
		boolean allConstants=true;
		for (Iterator<Expression> iterator=this.arguments.iterator(); iterator.hasNext() && allConstants;)
		{
			Expression argument=iterator.next();
			Expression reducedArgument=argument.reduce(evaluatorFactory);
			if (reducedArgument instanceof ConstantExpression)
			{
				reducedArguments.add(reducedArgument);
			}
			else
			{
				allConstants=false;
			}
		}
		if (allConstants)
		{
			reducedExpression=new FunctionCallExpression(this.functionObject, reducedArguments);
		}
		else
		{
			reducedExpression=this;
		}
		return reducedExpression;
	}
}
