package com.samajackun.sumascript.core.sources;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.Name;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.Source;
import com.samajackun.rodas.sql.engine.DefaultCursor;

public class ReferenceSource implements Source
{
	private static final long serialVersionUID=-2724701321524563313L;

	private final Name name;

	public ReferenceSource(Name name)
	{
		super();
		this.name=name;
	}

	@Override
	public String toCode()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor execute(Engine engine, Context context)
		throws EngineException,
		EvaluationException,
		ProviderException
	{
		Object value=context.getVariablesManager().peekLocalContext().get(this.name);
		if (value instanceof Collection)
		{
			@SuppressWarnings("unchecked")
			Collection<Map<String, Object>> collection=(Collection<Map<String, Object>>)value;
			List<ColumnMetaData> metadata=toColumnMetaData(collection);
			IterableTableData iterable=new CollectionIterableTableData(metadata, collection);
			return new DefaultCursor(metadata, iterable);
		}
		else
		{
			throw new EngineException("Variable " + this.name + " is expected to be a collection,");
		}
	}

	private List<ColumnMetaData> toColumnMetaData(Collection<Map<String, Object>> collection)
	{
		List<ColumnMetaData> list;
		if (collection.isEmpty())
		{
			list=Collections.emptyList();
		}
		else
		{
			Map<String, Object> map=collection.iterator().next();
			list=map.keySet().stream().map(x -> new ColumnMetaData(x, Datatype.TEXT, true)).collect(Collectors.toList());
		}
		return list;
	}
}
