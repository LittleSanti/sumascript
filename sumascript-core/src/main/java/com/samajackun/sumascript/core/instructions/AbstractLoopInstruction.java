package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

abstract class AbstractLoopInstruction implements Instruction
{
	private final Expression loopingCondition;

	private final Instruction stepInstruction;

	public AbstractLoopInstruction(Expression loopingCondition, Instruction stepInstruction)
	{
		super();
		this.loopingCondition=loopingCondition;
		this.stepInstruction=stepInstruction;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Jump jump=initializations(context);
			while (evaluateCondition(context))
			{
				jump=this.stepInstruction.execute(context);
				if (jump.isBreak())
				{
					jump=NoJump.getInstance();
					break;
				}
				else if (jump.isExit() || jump.isReturn() || jump.isThrow())
				{
					break;
				}
				jump=postInstructions(context);
				if (jump.isExit() || jump.isReturn() || jump.isThrow())
				{
					break;
				}
			}
			return jump;
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	protected boolean evaluateCondition(Context context)
		throws EvaluationException
	{
		return ConditionalsUtils.isTrue(this.loopingCondition.evaluate(context, ScriptEvaluatorFactory.getInstance()));
	}

	protected Jump initializations(Context context)
		throws ExecutionException
	{
		return NoJump.getInstance();
	}

	protected Jump postInstructions(Context context)
		throws ExecutionException
	{
		return NoJump.getInstance();
	}
}
