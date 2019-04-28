package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.ArithmeticUtils;
import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.expressions.Assignable;

public class MultiplyAssignationInstruction extends AssignationInstruction
{
	public MultiplyAssignationInstruction(Assignable leftSide, Expression rightSide)
	{
		super(leftSide, rightSide);
	}

	@Override
	protected Object computeValue(Context context, EvaluatorFactory evaluatorFactory, Assignable leftSide, Expression rightSide)
		throws EvaluationException
	{
		Object value1=leftSide.evaluate(context, evaluatorFactory);
		Object value2=rightSide.evaluate(context, evaluatorFactory);
		return ArithmeticUtils.computeMultiply(value1, value2);
	}
}