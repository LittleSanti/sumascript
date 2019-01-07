package com.samajackun.sumascript.core;

import com.samajackun.rodas.core.model.Expression;

public interface Jump
{
	public boolean isBreak();

	public boolean isReturn();

	public boolean isExit();

	public boolean isThrow();

	public Expression getExpression();
}
