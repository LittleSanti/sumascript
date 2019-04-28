package com.samajackun.sumascript.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;
import com.samajackun.sumascript.core.runtime.CodedFunction;

public class UnnamedFunctionDeclarationExpression implements Expression
{
	private final CodedFunction codedFunction;

	public UnnamedFunctionDeclarationExpression(CodedFunction codedFunction)
	{
		super();
		this.codedFunction=codedFunction;
	}

	public CodedFunction getCodedFunction()
	{
		return codedFunction;
	}

	@Override
	public String toCode()
	{
		// TODO
		StringBuilder stb=new StringBuilder();
		stb.append("function(" + codedFunction.getParameterNames().toString() + "){" + codedFunction.getBody().toString() + "}");
		return stb.toString();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return codedFunction;
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

}
