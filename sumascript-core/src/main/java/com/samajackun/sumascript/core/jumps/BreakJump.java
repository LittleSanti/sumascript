package com.samajackun.sumascript.core.jumps;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Jump;

public final class BreakJump implements Jump
{
	private static final BreakJump INSTANCE=new BreakJump();

	public static BreakJump getInstance()
	{
		return BreakJump.INSTANCE;
	}

	private BreakJump()
	{
	}

	@Override
	public boolean isBreak()
	{
		return true;
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
		return false;
	}

	@Override
	public Expression getExpression()
	{
		return null;
	}

}
