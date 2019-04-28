package com.samajackun.sumascript.core.instructions;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.QueryEvaluator;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.TableSource;

public class SumaQueryEvaluator implements QueryEvaluator
{
	private final EvaluatorFactory evaluatorFactory;

	private Engine engine;

	public SumaQueryEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super();
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public Object evaluate(Context context, SelectSentence select)
		throws EvaluationException
	{
		// TODO Hay que ejecutar toda la consulta y retornarla como colección (o stream) de maps.
		try
		{
			return this.engine.execute(select, context);
		}
		catch (EngineException | ProviderException e)
		{
			throw new EvaluationException(e);
		}
	}

	@Override
	public Object evaluate(Context context, TableSource table)
		throws EvaluationException
	{
		return null;
	}
}
