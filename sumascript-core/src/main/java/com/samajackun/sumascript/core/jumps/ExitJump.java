package com.samajackun.sumascript.core.jumps;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Jump;

public final class ExitJump implements Jump
{
	private static final ExitJump INSTANCE=new ExitJump();

	public static ExitJump getInstance()
	{
		return ExitJump.INSTANCE;
	}

	private ExitJump()
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
		return true;
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
