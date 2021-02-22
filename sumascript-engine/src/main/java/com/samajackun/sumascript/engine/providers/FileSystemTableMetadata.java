package com.samajackun.sumascript.engine.providers;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.Datatype;
import com.samajackun.rodas.core.model.TableData;
import com.samajackun.rodas.core.model.TableMetaData;

public class FileSystemTableMetadata implements TableMetaData
{
	private final String name;

	private final File path;

	private final List<ColumnMetaData> columns=Arrays.asList(new ColumnMetaData[] {
		new ColumnMetaData("name", Datatype.TEXT, false),
		new ColumnMetaData("size", Datatype.INTEGER_NUMBER, false),
		new ColumnMetaData("date", Datatype.DATETIME, false),
	});

	private final Map<String, Integer> columnMap=toMap(this.columns);

	private TableData tableData;

	public FileSystemTableMetadata(String name, File path)
	{
		super();
		this.name=name;
		this.path=path;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	private Map<String, Integer> toMap(List<ColumnMetaData> columns2)
	{
		Map<String, Integer> map=new HashMap<>();
		for (int i=0; i < columns2.size(); i++)
		{
			ColumnMetaData columnMetaData=columns2.get(i);
			map.put(columnMetaData.getName(), i);
		}
		return map;
	}

	@Override
	public ColumnMetaData getColumnMetadata(int column)
	{
		return this.columns.get(column);
	}

	@Override
	public List<ColumnMetaData> getListOfColumnMetadata()
	{
		return this.columns;
	}

	@Override
	public Map<String, Integer> getColumnMap()
	{
		return this.columnMap;
	}

	@Override
	public TableData getTableData()
	{
		if (this.tableData == null)
		{
			synchronized (this)
			{
				if (this.tableData == null)
				{
					this.tableData=new FileSystemTableData(this.path);
				}
			}
		}
		return this.tableData;
	}
}
