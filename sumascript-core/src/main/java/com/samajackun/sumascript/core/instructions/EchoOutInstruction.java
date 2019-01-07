package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class EchoOutInstruction implements Instruction
{
	private final Expression expression;

	public EchoOutInstruction(Expression expression)
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
			context.getRuntime().getOut().println(this.expression.evaluate(context, ScriptEvaluatorFactory.getInstance()));
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}
}
