package com.samajackun.sumascript.core.expressions;

import java.util.LinkedHashMap;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class MapExpression implements Expression
{
	private final Map<String, Expression> map=new LinkedHashMap<>();

	public MapExpression()
	{
		super();
	}

	public void put(String key, Expression value)
	{
		this.map.put(key, value);
	}

	@Override
	public String toCode()
	{
		String code="";
		for (Map.Entry<String, Expression> entry : this.map.entrySet())
		{
			if (code.length() > 0)
			{
				code+=",";
			}
			code+='"' + entry.getKey() + "\":" + entry.getValue().toCode();
		}
		code="{" + code + "}";
		return code;
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		Map<String, Object> object=new LinkedHashMap<>((int)(1.7d * this.map.size()));
		for (Map.Entry<String, Expression> entry : this.map.entrySet())
		{
			object.put(entry.getKey(), entry.getValue().evaluate(context, evaluatorFactory));
		}
		return object;
	}

	@Override
	public Datatype getDatatype(Context arg0, EvaluatorFactory arg1)
		throws MetadataException
	{
		return Datatype.OBJECT;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		MapExpression reducedExpression=new MapExpression();
		for (Map.Entry<String, Expression> entry : this.map.entrySet())
		{
			reducedExpression.put(entry.getKey(), entry.getValue().reduce(evaluatorFactory));
		}
		return reducedExpression;
	}
}
