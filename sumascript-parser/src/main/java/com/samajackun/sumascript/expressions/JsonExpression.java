package com.samajackun.sumascript.expressions;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class JsonExpression implements Expression
{
	private final LinkedHashMap<String, Expression> map;

	public JsonExpression(LinkedHashMap<String, Expression> map)
	{
		super();
		this.map=map;
	}

	@Override
	public String toCode()
	{
		StringBuilder stb=new StringBuilder(2048);
		stb.append('{');
		for (Iterator<Map.Entry<String, Expression>> iterator=this.map.entrySet().iterator(); iterator.hasNext();)
		{
			Map.Entry<String, Expression> entry=iterator.next();
			stb.append('"').append(entry.getKey()).append("\":").append(entry.getValue().toCode());
			if (iterator.hasNext())
			{
				stb.append(',');
			}
		}
		stb.append('}');
		return stb.toString();
	}

	@Override
	public Object evaluate(Context context, EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		LinkedHashMap<String, Object> evaluated=new LinkedHashMap<>((int)(1.7d * this.map.size()));
		for (Map.Entry<String, Expression> entry : this.map.entrySet())
		{
			evaluated.put(entry.getKey(), entry.getValue().evaluate(context, evaluatorFactory));
		}
		return evaluated;
	}

	@Override
	public Expression reduce(EvaluatorFactory evaluatorFactory)
		throws EvaluationException
	{
		LinkedHashMap<String, Expression> reduced=new LinkedHashMap<>((int)(1.7d * this.map.size()));
		for (Map.Entry<String, Expression> entry : this.map.entrySet())
		{
			reduced.put(entry.getKey(), entry.getValue().reduce(evaluatorFactory));
		}
		return new JsonExpression(reduced);
	}

	@Override
	public Datatype getDatatype(Context context, EvaluatorFactory evaluatorFactory)
		throws MetadataException
	{
		return Datatype.OBJECT;
	}

	public LinkedHashMap<String, Expression> getMap()
	{
		return this.map;
	}

}
