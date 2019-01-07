package com.samajackun.sumascript.core.jumps;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Jump;

public class ReturnJump implements Jump
{
	private final Expression expression;

	public ReturnJump(Expression expression)
	{
		super();
		this.expression=expression;
	}

	@Override
	public boolean isBreak()
	{
		return false;
	}

	@Override
	public boolean isReturn()
	{
		return true;
	}

	@Override
	public boolean isExit()
	{
		return false;
	}

	@Override
	public boolean isThrow()
	{
		return false;
	}

	@Override
	public Expression getExpression()
	{
		return this.expression;
	}
}
