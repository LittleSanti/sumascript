package com.samajackun.sumascript.core.expression;

import java.io.File;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.Runtime;
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

	private final VariablesManager variablesManager=new FlexibleVariablesManager(new FlexibleVariablesContext());

	private final EvaluatorFactory evaluatorFactory=SumaEvaluatorFactory.getInstance();

	@Override
	public Runtime getRuntime()
	{
		return this.runtime;
	}

	public ScriptRuntime getScriptRuntime()
	{
		return this.runtime;
	}

	@Override
	public VariablesManager getVariablesManager()
	{
		return this.variablesManager;
	}

	@Override
	public Provider getProvider()
	{
		return null;
	}

	@Override
	public Object evaluate(Expression expression, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		// Aqu� no pondremos cach�.
		return expression.evaluate(this, evaluatorFactory);
	}

	public void setRuntime(ScriptRuntime runtime)
	{
		this.runtime=runtime;
	}

	@Override
	public EvaluatorFactory getEvaluatorFactory()
	{
		return this.evaluatorFactory;
	}
}
