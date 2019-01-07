package com.samajackun.sumascript.core.expression;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.TextConstantExpression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.expressions.FunctionCallExpression;
import com.samajackun.sumascript.core.expressions.LocalVariableExpression;
import com.samajackun.sumascript.core.instructions.ScriptEvaluatorFactory;
import com.samajackun.sumascript.core.jumps.ReturnJump;
import com.samajackun.sumascript.core.runtime.CodedFunction;
import com.samajackun.sumascript.core.runtime.Function;
import com.samajackun.sumascript.core.runtime.FunctionCallException;

public class FunctionCallExpresionTest
{

	@Test
	public void codedFunctionWithOneParameter()
	{
		try
		{
			MyContext context=new MyContext();
			Instruction body=Mockito.mock(Instruction.class);
			when(body.execute(context)).thenReturn(new ReturnJump(new LocalVariableExpression("month")));
			List<String> parameterNames=Arrays.asList("month");
			Function function=new CodedFunction(parameterNames, body);
			Expression functionExpression=mock(Expression.class);
			when(functionExpression.evaluate(context, ScriptEvaluatorFactory.getInstance())).thenReturn(function);
			List<Expression> parameters=Arrays.asList(new TextConstantExpression("january"));
			FunctionCallExpression functionCall=new FunctionCallExpression(functionExpression, parameters);
			Object returnValue=functionCall.evaluate(context, ScriptEvaluatorFactory.getInstance());
			assertEquals("january", returnValue);
		}
		catch (ExecutionException | EvaluationException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void nativeFunctionWithOneParameter()
	{
		try
		{
			MyContext context=new MyContext();
			Function function=new Function()
			{
				@Override
				public Object call(Context context, List<Object> argumentValues)
					throws FunctionCallException
				{
					return argumentValues.get(0);
				}
			};
			Expression functionExpression=mock(Expression.class);
			when(functionExpression.evaluate(context, ScriptEvaluatorFactory.getInstance())).thenReturn(function);
			List<Expression> parameters=Arrays.asList(new TextConstantExpression("january"));
			FunctionCallExpression functionCall=new FunctionCallExpression(functionExpression, parameters);
			Object returnValue=functionCall.evaluate(context, ScriptEvaluatorFactory.getInstance());
			assertEquals("january", returnValue);
		}
		catch (EvaluationException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
}
