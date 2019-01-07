package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Instruction;

public abstract class AbstractVariableAssignation implements Instruction
{
	private final String name;

	private final Expression expression;

	protected AbstractVariableAssignation(String name, Expression expression)
	{
		super();
		this.name=name;
		this.expression=expression;
	}

	public String getName()
	{
		return this.name;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

}