package com.samajackun.sumascript.core.expression;

import java.io.File;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.VariablesManager;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.Provider;
import com.samajackun.sumascript.core.instructions.SumaEvaluatorFactory;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesContext;
import com.samajackun.sumascript.core.runtime.FlexibleVariablesManager;
import com.samajackun.sumascript.core.runtime.ScriptRuntime;

public class MyContext implements Context
{
	private ScriptRuntime runtime=new ScriptRuntime(new File(System.getProperty("user.dir")));

	private VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());

	private EvaluatorFactory evaluatorFactory=SumaEvaluatorFactory.getInstance();

	private Provider provider;

	@Override
	public ScriptRuntime getRuntime()
	{
		return this.runtime;
	}

	public void setRuntime(ScriptRuntime runtime)
	{
		this.runtime=runtime;
	}

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	public void setVariablesManager(VariablesManager variablesManager)
	{
		this.variablesManager=variablesManager;
	}

	@Override
	public EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}

	public void setEvaluatorFactory(EvaluatorFactory evaluatorFactory)
	{
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public Provider getProvider()
	{
		return this.provider;
	}

	public void setProvider(Provider provider)
	{
		this.provider=provider;
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		return expression.evaluate(this, evaluatorFactory);
	}

}
