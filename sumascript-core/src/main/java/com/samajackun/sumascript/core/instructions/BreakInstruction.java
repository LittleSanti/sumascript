package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.BreakJump;

public class BreakInstruction implements Instruction
{
	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		return BreakJump.getInstance();
	}

}
