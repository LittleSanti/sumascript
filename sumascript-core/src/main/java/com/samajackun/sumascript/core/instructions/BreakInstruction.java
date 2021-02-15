package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.BreakJump;

public final class BreakInstruction implements Instruction
{
	private static final long serialVersionUID=-4360724533032190553L;

	private static final BreakInstruction INSTANCE=new BreakInstruction();

	public static BreakInstruction getInstance()
	{
		return INSTANCE;
	}

	private BreakInstruction()
	{
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		return BreakJump.getInstance();
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeBreak(this);
	}
}
