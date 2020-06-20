package com.samajackun.sumascript.expressions;

import java.util.Collections;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.runtime.CodedFunction;

public class UnnamedFunctionDeclarationExpression implements Expression
{
	private static final long serialVersionUID=-3906296455686881324L;

	private final CodedFunction codedFunction;

	public UnnamedFunctionDeclarationExpression(CodedFunction codedFunction)
	{
		super();
		this.codedFunction=codedFunction;
	}

	public CodedFunction getCodedFunction()
	{
		return this.codedFunction;
	}

	@Override
	public String toCode()
	{
		// TODO
		StringBuilder stb=new StringBuilder();
		stb.append("function(" + this.codedFunction.getParameterNames().toString() + "){" + this.codedFunction.getBody().toString() + "}");
		return stb.toString();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this.codedFunction;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return this;
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.FUNCTION;
	}

	@Override
	public List<Expression> getSubExpressions()
	{
		return Collections.emptyList();
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.codedFunction == null)
			? 0
			: this.codedFunction.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		UnnamedFunctionDeclarationExpression other=(UnnamedFunctionDeclarationExpression)obj;
		if (this.codedFunction == null)
		{
			if (other.codedFunction != null)
			{
				return false;
			}
		}
		else if (!this.codedFunction.equals(other.codedFunction))
		{
			return false;
		}
		return true;
	}
}
