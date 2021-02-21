package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.ArithmeticUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.expressions.Assignable;
import com.samajackun.sumascript.core.jumps.NoJump;

public class DecrementInstruction implements Instruction
{
	private static final long serialVersionUID=6396490850487012828L;

	private final Assignable leftSide;

	public DecrementInstruction(Assignable leftSide)
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
			Object value=this.leftSide.evaluate(context, context.getEvaluatorFactory());
			this.leftSide.set(context, context.getEvaluatorFactory(), ArithmeticUtils.computeAdd(value, -1));
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeDecrement(this);
	}

	public Assignable getLeftSide()
	{
		return this.leftSide;
	}
}
