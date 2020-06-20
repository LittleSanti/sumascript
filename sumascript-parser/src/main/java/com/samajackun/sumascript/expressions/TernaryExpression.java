package com.samajackun.sumascript.expressions;

import java.util.ArrayList;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.ConstantExpression;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.instructions.ConditionalsUtils;

public class TernaryExpression implements Expression
{
	private static final long serialVersionUID=5620979812746429280L;

	private final List<Expression> operands=new ArrayList<>(3);

	public TernaryExpression(Expression conditional, Expression direct, Expression inverse)
	{
		super();
		this.operands.add(conditional);
		this.operands.add(direct);
		this.operands.add(inverse);
	}

	public Expression getConditional()
	{
		return this.operands.get(0);
	}

	public Expression getDirect()
	{
		return this.operands.get(1);
	}

	public Expression getInverse()
	{
		return this.operands.get(2);
	}

	@Override
	public String toCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Object conditionalValue=this.operands.get(0).evaluate(context, evaluatorFactory);
		Object result=ConditionalsUtils.isTrue(conditionalValue)
			? this.operands.get(1).evaluate(context, evaluatorFactory)
			: this.operands.get(2).evaluate(context, evaluatorFactory);
		return result;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Expression reduced;
		Expression reducedDirect=this.operands.get(1).reduce(evaluatorFactory);
		Expression reducedInverse=this.operands.get(2).reduce(evaluatorFactory);
		Expression reducedConditional=this.operands.get(0).reduce(evaluatorFactory);
		if (reducedConditional instanceof ConstantExpression)
		{
			Object conditionalValue=((ConstantExpression)reducedConditional).getValue();
			reduced=ConditionalsUtils.isTrue(conditionalValue)
				? reducedDirect
				: reducedInverse;
		}
		else
		{
			reduced=new TernaryExpression(reducedConditional, reducedDirect, reducedInverse);
		}
		return reduced;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		Datatype d1=this.operands.get(1).getDatatype(context, evaluatorFactory);
		Datatype d2=this.operands.get(2).getDatatype(context, evaluatorFactory);
		return d1 == d2
			? d1
			: Datatype.UNKNOWN;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return this.operands;
	}
}
