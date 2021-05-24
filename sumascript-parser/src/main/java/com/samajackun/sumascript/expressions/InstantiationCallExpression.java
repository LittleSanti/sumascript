package com.samajackun.sumascript.expressions;

import java.util.List;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.FunctionCallExpression;

public class InstantiationCallExpression extends FunctionCallExpression
{
	private static final long serialVersionUID=-8559862447950301507L;

	public InstantiationCallExpression(Expression functionObject, List<Expression> arguments)
	{
		super(functionObject, arguments);
	}
}
