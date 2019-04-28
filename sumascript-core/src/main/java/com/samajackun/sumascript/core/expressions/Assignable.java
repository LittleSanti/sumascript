package com.samajackun.sumascript.core.expressions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Expression;

public interface Assignable extends Expression
{
	public void set(Context context, EvaluatorFactory evaluatorFactory, Object value)
		throws EvaluationException;
}
