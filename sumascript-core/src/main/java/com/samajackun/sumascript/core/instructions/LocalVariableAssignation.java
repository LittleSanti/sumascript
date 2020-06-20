package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;
import com.samajackun.sumascript.core.runtime.Undefined;

public class LocalVariableAssignation extends AbstractVariableAssignation
{
	private static final long serialVersionUID=-372653816020600693L;

	public LocalVariableAssignation(Name name, Expression expression)
	{
		super(name, expression);
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=getExpression() == null
				? Undefined.getInstance()
				: getExpression().evaluate(context, SumaEvaluatorFactory.getInstance());
			context.getVariablesManager().setLocalVariable(getName(), value);
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}
}
