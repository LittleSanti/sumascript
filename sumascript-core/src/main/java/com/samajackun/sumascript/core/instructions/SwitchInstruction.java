package com.samajackun.sumascript.core.instructions;

import java.util.List;
import java.util.Set;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.jumps.NoJump;

public class SwitchInstruction implements Instruction
{
	private final Expression expression;

	private final List<Pair> pairs;

	public SwitchInstruction(Expression expression, List<Pair> pairs)
	{
		super();
		this.expression=expression;
		this.pairs=pairs;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Jump jump=NoJump.getInstance();
			Object value=this.expression.evaluate(context, ScriptEvaluatorFactory.getInstance());
			boolean executing=false;
			for (Pair pair : this.pairs)
			{
				if (!executing)
				{
					if (pair.getMatchingValues().contains(value))
					{
						executing=true;
					}
				}
				if (executing)
				{
					jump=pair.getInstruction().execute(context);
					if (jump.isBreak())
					{
						jump=NoJump.getInstance();
						break;
					}
					else if (jump.isReturn() || jump.isThrow() || jump.isExit())
					{
						break;
					}
				}
			}
			return jump;
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	public static class Pair
	{
		private final Set<Object> matchingValues;

		private final Instruction instruction;

		public Pair(Set<Object> matchingValues, Instruction instruction)
		{
			super();
			this.matchingValues=matchingValues;
			this.instruction=instruction;
		}

		public Instruction getInstruction()
		{
			return this.instruction;
		}

		public Set<Object> getMatchingValues()
		{
			return this.matchingValues;
		}
	}
}
