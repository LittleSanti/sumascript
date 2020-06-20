package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.expressions.Assignable;
import com.samajackun.sumascript.core.jumps.NoJump;

public class AssignationInstruction implements Instruction
{
	private static final long serialVersionUID=8524622322183083120L;

	private final Assignable leftSide;

	private final Expression rightSide;

	public AssignationInstruction(Assignable leftSide, Expression rightSide)
	{
		super();
		this.leftSide=leftSide;
		this.rightSide=rightSide;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=computeValue(context, SumaEvaluatorFactory.getInstance(), this.leftSide, this.rightSide);
			this.leftSide.set(context, SumaEvaluatorFactory.getInstance(), value);
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	protected Object computeValue(Context context, EvaluatorFactory evaluatorFactory, Assignable leftSide2, Expression rightSide2)
		throws EvaluationException
	{
		return rightSide2.evaluate(context, SumaEvaluatorFactory.getInstance());
	}
	}
