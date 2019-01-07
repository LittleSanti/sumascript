package com.samajackun.sumascript.core.instructions;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.expression.MyContext;
import com.samajackun.sumascript.core.instructions.IfInstruction;
import com.samajackun.sumascript.core.instructions.ScriptEvaluatorFactory;

public class IfInstructionTest
{

	@Test
	public void trueCondition()
	{
		try
		{
			MyContext context=new MyContext();
			Expression conditionalExpression=Mockito.mock(Expression.class);
			Mockito.when(conditionalExpression.evaluate(context, ScriptEvaluatorFactory.getInstance())).thenReturn(true);

			Instruction positiveInstruction=Mockito.mock(Instruction.class);
			Instruction negativeInstruction=Mockito.mock(Instruction.class);
			IfInstruction ifInstruction=new IfInstruction(conditionalExpression, positiveInstruction, negativeInstruction);
			ifInstruction.execute(context);
			Mockito.verify(positiveInstruction).execute(context);
			Mockito.verify(negativeInstruction, Mockito.times(0)).execute(context);
		}
		catch (ExecutionException | EvaluationException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}

	@Test
	public void falseCondition()
	{
		try
		{
			MyContext context=new MyContext();
			Expression conditionalExpression=Mockito.mock(Expression.class);
			Mockito.when(conditionalExpression.evaluate(context, ScriptEvaluatorFactory.getInstance())).thenReturn(false);

			Instruction positiveInstruction=Mockito.mock(Instruction.class);
			Instruction negativeInstruction=Mockito.mock(Instruction.class);
			IfInstruction ifInstruction=new IfInstruction(conditionalExpression, positiveInstruction, negativeInstruction);
			ifInstruction.execute(context);
			Mockito.verify(negativeInstruction).execute(context);
			Mockito.verify(positiveInstruction, Mockito.times(0)).execute(context);
		}
		catch (ExecutionException | EvaluationException e)
		{
			e.printStackTrace();
			Assert.fail(e.toString());
		}
	}
}
