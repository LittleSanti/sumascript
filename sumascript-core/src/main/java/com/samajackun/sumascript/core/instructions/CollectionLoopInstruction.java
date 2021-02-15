package com.samajackun.sumascript.core.instructions;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;

public class CollectionLoopInstruction extends AbstractLoopInstruction
{
	private static final long serialVersionUID=-2983484473855048707L;

	private final Name varName;

	private final Expression expressionCollection;

	private Iterator<Object> iterator;

	public CollectionLoopInstruction(Expression preCondition, Instruction innerInstruction, Expression postCondition, Name varName, Expression expressionCollection)
	{
		super(preCondition, innerInstruction, postCondition);
		this.varName=varName;
		this.expressionCollection=expressionCollection;
	}

	@Override
	protected Jump initializations(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=this.expressionCollection.evaluate(context, SumaEvaluatorFactory.getInstance());
			if (value instanceof Collection)
			{
				@SuppressWarnings("unchecked")
				Collection<Object> collection=(Collection<Object>)value;
				this.iterator=collection.iterator();
			}
			else if (value instanceof Map)
			{
				@SuppressWarnings("unchecked")
				Map<Object, Object> map=(Map<Object, Object>)value;
				this.iterator=map.keySet().iterator();
			}
			else
			{
				this.iterator=Collections.singleton(value).iterator();
			}
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	@Override
	protected boolean evaluatePreCondition(Context context)
		throws EvaluationException
	{
		boolean x=this.iterator.hasNext();
		if (x)
		{
			x=super.evaluatePreCondition(context);
			if (x)
			{
				context.getVariablesManager().setLocalVariable(this.varName, this.iterator.next());
			}
		}
		return x;
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeCollectionLoop(this);
	}

	public Name getVarName()
	{
		return this.varName;
	}

	public Expression getExpressionCollection()
	{
		return this.expressionCollection;
	}
}
