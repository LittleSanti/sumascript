package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Instruction;

public class WhileLoopInstruction extends AbstractLoopInstruction
{
	public WhileLoopInstruction(Expression loopingCondition, Instruction stepInstruction)
	{
		super(loopingCondition, stepInstruction);
	}
}
