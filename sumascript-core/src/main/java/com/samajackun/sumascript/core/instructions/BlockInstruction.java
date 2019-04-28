package com.samajackun.sumascript.core.instructions;

import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class BlockInstruction implements Instruction
{
	private final List<Instruction> instructions;

	public BlockInstruction(List<Instruction> instructions)
	{
		super();
		this.instructions=instructions;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		Jump jump=NoJump.getInstance();
		for (Iterator<Instruction> iterator=this.instructions.iterator(); iterator.hasNext() && canGoOn(jump);)
		{
			Instruction instruction=iterator.next();
			jump=instruction.execute(context);
		}
		return jump;
	}

	private boolean canGoOn(Jump jump)
	{
		return !(jump.isBreak() || jump.isExit() || jump.isReturn() || jump.isThrow());
	}
}
