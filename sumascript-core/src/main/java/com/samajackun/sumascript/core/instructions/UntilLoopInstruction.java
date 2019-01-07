package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.Instruction;

public class UntilLoopInstruction extends AbstractLoopInstruction
{
	public UntilLoopInstruction(Expression loopingCondition, Instruction stepInstruction)
	{
		super(loopingCondition, stepInstruction);
	}

	@Override
	protected boolean evaluateCondition(Context context)
		throws EvaluationException
	{
		return !super.evaluateCondition(context);
	}
}
