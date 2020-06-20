package com.samajackun.sumascript.expressions;

import java.util.Iterator;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.ExpressionList;

public class ArrayExpression extends ExpressionList
{
	private static final long serialVersionUID=1147881606659694165L;

	public ArrayExpression()
	{
	}

	public ArrayExpression(int size)
	{
		super(size);
	}

	@Override
	public String toCode()
	{
		StringBuilder stb=new StringBuilder(100 * getExpressions().size());
		stb.append('[');
		for (Iterator<Expression> iterator=getExpressions().iterator(); iterator.hasNext();)
		{
			Expression expression=iterator.next();
			stb.append(expression.toCode());
			if (iterator.hasNext())
			{
				stb.append(',');
			}
		}
		stb.append(']');
		return stb.toString();
	}

}
