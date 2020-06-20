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
	private static final long serialVersionUID=4715129974263967644L;

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
			Object value=this.expression.evaluate(context, SumaEvaluatorFactory.getInstance());
			Jump jump;
			if (ConditionalsUtils.isTrue(value))
			{
				jump=(this.afirmative == null)
					? NoJump.getInstance()
					: this.afirmative.execute(context);
			}
			else
			{
				jump=(this.negative == null)
					? NoJump.getInstance()
					: this.negative.execute(context);
			}
			return jump;
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

}
