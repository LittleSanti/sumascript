package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.ArithmeticUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.expressions.Assignable;
import com.samajackun.sumascript.core.jumps.NoJump;

public class IncrementInstruction implements Instruction
{
	private final Assignable leftSide;

	public IncrementInstruction(Assignable leftSide)
	{
		super();
		this.leftSide=leftSide;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=this.leftSide.evaluate(context, SumaEvaluatorFactory.getInstance());
			this.leftSide.set(context, SumaEvaluatorFactory.getInstance(), ArithmeticUtils.computeAdd(value, 1));
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}
}