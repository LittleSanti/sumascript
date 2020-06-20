package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Instruction;

public abstract class AbstractVariableAssignation implements Instruction
{
	private static final long serialVersionUID=4280413252521649723L;

	private final Name name;

	private final Expression expression;

	protected AbstractVariableAssignation(Name name, Expression expression)
	{
		super();
		this.name=name;
		this.expression=expression;
	}

	public Name getName()
	{
		return this.name;
	}

	public Expression getExpression()
	{
		return this.expression;
	}

}