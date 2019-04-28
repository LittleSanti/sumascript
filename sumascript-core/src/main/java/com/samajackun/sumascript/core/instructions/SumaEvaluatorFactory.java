package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.FunctionEvaluator;
import com.samajackun.rodas.core.eval.QueryEvaluator;
import com.samajackun.rodas.core.eval.evaluators.DefaultEvaluatorFactory;

public final class SumaEvaluatorFactory extends DefaultEvaluatorFactory
{
	private static final SumaEvaluatorFactory INSTANCE=new SumaEvaluatorFactory();

	private FunctionEvaluator myFunctionEvaluator;

	private QueryEvaluator myQueryEvaluator;

	public static SumaEvaluatorFactory getInstance()
	{
		return INSTANCE;
	}

	private SumaEvaluatorFactory()
	{
	}

	@Override
	public FunctionEvaluator getFunctionEvaluator()
	{
		if (this.myFunctionEvaluator == null)
		{
			synchronized (this)
			{
				if (this.myFunctionEvaluator == null)
				{
					this.myFunctionEvaluator=new SumaFunctionEvaluator(SumaEvaluatorFactory.getInstance());
				}
			}
		}
		return this.myFunctionEvaluator;
	}

	@Override
	public QueryEvaluator getQueryEvaluator()
	{
		if (this.myQueryEvaluator == null)
		{
			synchronized (this)
			{
				if (this.myQueryEvaluator == null)
				{
					this.myQueryEvaluator=new SumaQueryEvaluator(SumaEvaluatorFactory.getInstance());
				}
			}
		}
		return this.myQueryEvaluator;
	}
}
