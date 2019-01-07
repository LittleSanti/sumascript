package com.samajackun.sumascript.core.jumps;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Jump;

public final class NoJump implements Jump
{
	private static final NoJump INSTANCE=new NoJump();

	public static NoJump getInstance()
	{
		return NoJump.INSTANCE;
	}

	private NoJump()
	{
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
		return false;
	}

	@Override
	public Expression getExpression()
	{
		return null;
	}

}
