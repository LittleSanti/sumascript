package com.samajackun.sumascript.core.instructions;

import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.sumascript.core.ExecutionException;
import com.samajackun.sumascript.core.Instruction;
import com.samajackun.sumascript.core.Jump;
import com.samajackun.sumascript.core.SumaInstructionSerializerException;
import com.samajackun.sumascript.core.jumps.NoJump;

public class IndexedAssignationInstruction implements Instruction
{
	private static final long serialVersionUID=8759349115486720055L;

	private final Expression base;

	private final Expression index;

	private final Expression expression;

	public IndexedAssignationInstruction(Expression base, Expression index, Expression expression)
	{
		super();
		this.base=base;
		this.index=index;
		this.expression=expression;
	}

	@Override
	public Jump execute(Context context)
		throws ExecutionException
	{
		try
		{
			Object value=this.expression.evaluate(context, context.getEvaluatorFactory());
			Object indexValue=this.index.evaluate(context, context.getEvaluatorFactory());
			Object baseValue=this.base.evaluate(context, context.getEvaluatorFactory());
			if (baseValue instanceof List)
			{
				if (indexValue instanceof Integer)
				{
					@SuppressWarnings("unchecked")
					List<Object> list=(List<Object>)baseValue;
					int p=((Number)indexValue).intValue();
					if (p >= list.size())
					{
						list.add(p, value);
					}
					else
					{
						list.set(p, value);
					}
				}
				else
				{
					throw new ExecutionException("An integer value is required to index an array, instead of '" + indexValue + "'");
				}
			}
			else if (baseValue instanceof Map)
			{
				@SuppressWarnings("unchecked")
				Map<Object, Object> map=(Map<Object, Object>)baseValue;
				value=map.put(indexValue, value);
			}
			else
			{
				throw new ExecutionException("An array or map is required to be indexed, instead of '" + baseValue + "'");
			}
			return NoJump.getInstance();
		}
		catch (EvaluationException e)
		{
			throw new ExecutionException(e);
		}
	}

	@Override
	public String toCode(SumaInstructionSerializer serializer)
		throws SumaInstructionSerializerException
	{
		return serializer.serializeIndexedAssignation(this);
	}

	public Expression getBase()
	{
		return this.base;
	}

	public Expression getIndex()
	{
		return this.index;
	}

	public Expression getExpression()
	{
		return this.expression;
	}
}
