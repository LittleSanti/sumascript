package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class GlobalVariableAssignation extends AbstractVariableAssignation
{
	public GlobalVariableAssignation(String name, Expression expression)
	{
		super(name, expression);
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			context.getVariablesManager().setGlobalVariable(getName(), getExpression().evaluate(context, ScriptEvaluatorFactory.getInstance()));
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}
}
