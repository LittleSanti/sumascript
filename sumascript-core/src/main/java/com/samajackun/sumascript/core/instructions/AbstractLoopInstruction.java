package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public abstract class AbstractLoopInstruction implements Instruction
{
	private static final long serialVersionUID=-2144776615610322307L;

	private final Expression preCondition;

	private final Expression postCondition;

	private final Instruction innerInstruction;

	public AbstractLoopInstruction(Expression preCondition, Instruction innerInstruction, Expression postCondition)
	{
		super();
		this.preCondition=preCondition;
		this.innerInstruction=innerInstruction;
		this.postCondition=postCondition;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Jump jump=initializations(context);
			boolean looping=true;
			while (looping)
			{
				looping=evaluatePreCondition(context);
				if (looping)
				{
					jump=this.innerInstruction.execute(context);
					if (jump.isBreak())
					{
						jump=NoJump.getInstance();
						looping=false;
					}
					else if (jump.isExit() || jump.isReturn() || jump.isThrow())
					{
						looping=false;
					}
					jump=postInstructions(context);
					if (jump.isExit() || jump.isReturn() || jump.isThrow())
					{
						looping=false;
					}
				}
				if (looping)
				{
					looping=evaluatePostCondition(context);
				}
			}
			return jump;
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	protected boolean evaluatePreCondition(Context context)
		throws EvaluationException
	{
		return this.preCondition == null || ConditionalsUtils.isTrue(this.preCondition.evaluate(context, context.getEvaluatorFactory()));
	}

	protected boolean evaluatePostCondition(Context context)
		throws EvaluationException
	{
		return this.postCondition == null || ConditionalsUtils.isTrue(this.postCondition.evaluate(context, context.getEvaluatorFactory()));
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

	public Expression getPreCondition()
	{
		return this.preCondition;
	}

	public Expression getPostCondition()
	{
		return this.postCondition;
	}

	public Instruction getInnerInstruction()
	{
		return this.innerInstruction;
	}

}
