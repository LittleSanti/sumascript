package com.samajackun.sumascript.core.instructions;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class VariableAssignationsInstruction implements Instruction
{
	private final List<AbstractVariableAssignation> list;

	public VariableAssignationsInstruction(List<AbstractVariableAssignation> list)
	{
		super();
		this.list=list;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		for (AbstractVariableAssignation variable : this.list)
		{
			variable.execute(context);
		}
		return NoJump.getInstance();
	}
}
