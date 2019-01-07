package com.samajackun.sumascript.core.runtime;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.instructions.ScriptEvaluatorFactory;

public class CodedFunction implements Function
{
	private static final String PARAMETERS_NAME="arguments";

	private final List<String> parameterNames;

	private final Instruction body;

	public CodedFunction(List<String> parameterNames, Instruction body)
	{
		super();
		this.parameterNames=parameterNames;
		this.body=body;
	}

	@Override
	public Object call(Context context, List<Object> argumentValues)
		throws FunctionCallException
	{
		context.getVariablesManager().pushLocalContext();
		context.getVariablesManager().setLocalVariable(PARAMETERS_NAME, argumentValues);
		int p=0;
		for (String parameterName : this.parameterNames)
		{
			context.getVariablesManager().setLocalVariable(parameterName, argumentValues.get(p));
			p++;
		}
		try
		{
			Jump jump=this.body.execute(context);
			Object returnValue;
			if (jump.isReturn())
			{
				returnValue=jump.getExpression().evaluate(context, ScriptEvaluatorFactory.getInstance());
			}
			else
			{
				returnValue=Undefined.getInstance();
			}
			return returnValue;
		}
		catch (ExecutionException e)
		{
			throw new FunctionCallException(e);
		}
		catch (EvaluationException e)
		{
			throw new FunctionCallException(e);
		}
		finally
		{
			// No me molesto en llamar a removeLocalVariable, pues a continuación viene un pop.
			context.getVariablesManager().popLocalContext();
		}
	}
}
