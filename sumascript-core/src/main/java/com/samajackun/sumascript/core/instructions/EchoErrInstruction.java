package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;

public class EchoErrInstruction implements Instruction
{
	private static final long serialVersionUID=-3718856097420096051L;

	private final Expression expression;

	public EchoErrInstruction(Expression expression)
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
			context.getRuntime().getErr().println(this.expression.evaluate(context, context.getEvaluatorFactory()));
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
		return serializer.serializeEchoErr(this);
	}

	public Expression getExpression()
	{
		return this.expression;
	}
}
