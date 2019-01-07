package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.ThrowJump;

public class ThrowInstruction implements Instruction
{
	private final Expression thrownExpression;

	public ThrowInstruction(Expression thrownExpression)
	{
		super();
		this.thrownExpression=thrownExpression;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		return new ThrowJump(this.thrownExpression);
	}
}
