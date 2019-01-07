package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class IfInstruction implements Instruction
{
	private final Expression expression;

	private final Instruction afirmative;

	private final Instruction negative;

	public IfInstruction(Expression expression, Instruction afirmative, Instruction negative)
	{
		super();
		this.expression=expression;
		this.afirmative=afirmative;
		this.negative=negative;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=this.expression.evaluate(context, ScriptEvaluatorFactory.getInstance());
			Jump jump;
			if (ConditionalsUtils.isTrue(value))
			{
				jump=this.afirmative.execute(context);
			}
			else
			{
				if (this.negative != null)
				{
					jump=this.negative.execute(context);
				}
				else
				{
					jump=NoJump.getInstance();
				}
			}
			return jump;
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

}
