package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.ReturnJump;

public class ReturnInstruction implements Instruction
{
	private static final long serialVersionUID=1644278302071638432L;

	private final Expression expression;

	public ReturnInstruction(Expression expression)
	{
		super();
		this.expression=expression;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		return new ReturnJump(this.expression);
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeReturn(this);
	}

	public Expression getExpression()
	{
		return this.expression;
	}
}
