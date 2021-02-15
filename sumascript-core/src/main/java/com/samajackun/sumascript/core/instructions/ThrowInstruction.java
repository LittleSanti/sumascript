package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.ThrowJump;

public class ThrowInstruction implements Instruction
{
	private static final long serialVersionUID=7740934042636077853L;

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

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeThrow(this);
	}

	public Expression getThrownExpression()
	{
		return this.thrownExpression;
	}
}
