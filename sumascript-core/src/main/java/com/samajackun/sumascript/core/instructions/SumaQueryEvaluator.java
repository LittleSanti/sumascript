package com.samajackun.sumascript.core.instructions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.eval.Context;
import com.samajackun.rodas.core.eval.EvaluationException;
import com.samajackun.rodas.core.eval.EvaluatorFactory;
import com.samajackun.rodas.core.eval.QueryEvaluator;
import com.samajackun.rodas.core.execution.Cursor;
import com.samajackun.rodas.core.model.ColumnMetadata;
import com.samajackun.rodas.core.model.Engine;
import com.samajackun.rodas.core.model.EngineException;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.SelectSentence;
import com.samajackun.rodas.core.model.TableSource;
import com.samajackun.rodas.sql.engine.MyEngine;

public class SumaQueryEvaluator implements QueryEvaluator
{
	private final EvaluatorFactory evaluatorFactory;

	private final Engine engine=new MyEngine();

	public SumaQueryEvaluator(EvaluatorFactory evaluatorFactory)
	{
		super();
		this.evaluatorFactory=evaluatorFactory;
	}

	@Override
	public Object evaluate(Context context, SelectSentence select)
		throws EvaluationException
	{
		// Hay que ejecutar toda la consulta y retornarla como colección (o stream) de maps.
		// TODO Hay que optimizar este algoritmo y el de toRow, por supuesto.
		try
		{
			Cursor cursor=this.engine.execute(select, context);
			Collection<Map<String, Object>> rows=new ArrayList<>(2048);
			while (cursor.hasNext())
			{
				cursor.next();
				rows.add(toRow(cursor.getMetadata(), cursor.getColumnMap(), cursor.getRowData()));
			}
			return rows;
		}
		catch (EngineException | ProviderException e)
		{
			throw new EvaluationException(e);
		}
	}

	private Map<String, Object> toRow(List<ColumnMetadata> metadata, Map<String, Integer> columnsMap, RowData rowData)
	{
		Map<String, Object> row=new HashMap<>((int)(1.7d * metadata.size()));
		for (ColumnMetadata column : metadata)
		{
			String colName=column.getName();
			Integer colIndex=columnsMap.get(colName);
			Object colValue=rowData.get(colIndex);
			row.put(colName, colValue);
		}
		return row;
	}

	@Override
	public Object evaluate(Context context, TableSource table)
		throws EvaluationException
	{
		return null;
	}
}
