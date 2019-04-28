package com.samajackun.sumascript.parser;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;

public class InvalidLeftSideException extends ParserException
{
	private static final long serialVersionUID=-1285029853656861292L;

	private final Expression invalidExpression;

	public InvalidLeftSideException(Expression invalidExpression)
	{
		super("Invalid left side expression: It must be an Assignable, instead of " + invalidExpression.getClass().getName());
		this.invalidExpression=invalidExpression;
	}

	public Expression getInvalidExpression()
	{
		return this.invalidExpression;
	}
}
