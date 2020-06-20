package com.samajackun.sumascript.expressions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.MetadataException;

public class JsonExpression implements Expression
{
	private static final long serialVersionUID=5138947492927924925L;

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

	@Override
	public List<Expression> getSubExpressions()
	{
		return new ArrayList<>(this.map.values());
	}

	@Override
	public int hashCode()
	{
		final int prime=31;
		int result=1;
		result=prime * result + ((this.map == null)
			? 0
			: this.map.hashCode());
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
		JsonExpression other=(JsonExpression)obj;
		if (this.map == null)
		{
			if (other.map != null)
			{
				return false;
			}
		}
		else if (!this.map.equals(other.map))
		{
			return false;
		}
		return true;
	}
}
