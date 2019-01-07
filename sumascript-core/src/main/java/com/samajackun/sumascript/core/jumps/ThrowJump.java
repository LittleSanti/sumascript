package com.samajackun.sumascript.core.jumps;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Jump;

public class ThrowJump implements Jump
{
	private final Expression thrownExpression;

	public ThrowJump(Expression thrownExpression)
	{
		super();
		this.thrownExpression=thrownExpression;
	}

	@Override
	public boolean isBreak()
	{
		return false;
	}

	@Override
	public boolean isReturn()
	{
		return false;
	}

	@Override
	public boolean isExit()
	{
		return false;
	}

	@Override
	public boolean isThrow()
	{
		return true;
	}

	@Override
	public Expression getExpression()
	{
		return this.thrownExpression;
	}
}
