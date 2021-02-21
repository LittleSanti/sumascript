package com.samajackun.sumascript.core.runtime;

import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.eval.functions.Function;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;

public class CodedFunction implements Function
{
	private static final Name PARAMETERS_NAME=Name.instanceOf("arguments");

	private final List<Name> parameterNames;

	private final Instruction body;

	public CodedFunction(List<Name> parameterNames, Instruction body)
	{
		super();
		this.parameterNames=parameterNames;
		this.body=body;
	}

	public List<Name> getParameterNames()
	{
		return this.parameterNames;
	}

	public Instruction getBody()
	{
		return this.body;
	}

	@Override
	public Object call(Context context, List<Object> argumentValues)
		throws FunctionEvaluationException
	{
		context.getVariablesManager().pushLocalContext(new FlexibleVariablesContext());
		context.getVariablesManager().setLocalVariable(PARAMETERS_NAME, argumentValues);
		int p=0;
		for (Name parameterName : this.parameterNames)
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
				returnValue=jump.getExpression().evaluate(context, context.getEvaluatorFactory());
			}
			else
			{
				returnValue=Undefined.getInstance();
			}
			return returnValue;
		}
		catch (ExecutionException e)
		{
			throw new FunctionEvaluationException(e.getMessage(), "?");
		}
		catch (EvaluationException e)
		{
			throw new FunctionEvaluationException(e.getMessage(), "?");
		}
		finally
		{
			// No me molesto en llamar a removeLocalVariable, pues a continuaci�n viene un pop.
			context.getVariablesManager().popLocalContext();
		}
	}
}
