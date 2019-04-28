package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class ExpressionInstruction implements Instruction
{
	private final Expression expression;

	public ExpressionInstruction(Expression expression)
	{
		super();
		this.expression=expression;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			this.expression.evaluate(context, SumaEvaluatorFactory.getInstance());
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}
}
