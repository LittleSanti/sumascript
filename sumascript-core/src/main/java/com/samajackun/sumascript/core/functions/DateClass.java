package com.samajackun.sumascript.core.functions;

import static com.samajackun.rodas.core.eval.functions.Casts.asNumber;

import java.time.LocalDate;
import java.util.List;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.functions.AbstractFunction;
import com.samajackun.rodas.core.eval.functions.FunctionEvaluationException;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;

public class DateClass extends AbstractFunction
{
	public DateClass()
	{
		super("Date", SumaEvaluatorFactory.getInstance(), 3, 3);
	}

	@Override
	protected Object evaluateFunction(Context context, List<Object> values)
		throws FunctionEvaluationException
	{
		return LocalDate.of((int)asNumber("Date", "year", values.get(0)), (int)asNumber("Date", "month", values.get(1)), (int)asNumber("Date", "day", values.get(2)));
	}

}
