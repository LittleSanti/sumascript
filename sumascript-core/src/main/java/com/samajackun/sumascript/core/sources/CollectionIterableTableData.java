package com.samajackun.sumascript.core.sources;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.samajackun.rodas.core.model.ColumnMetaData;
import com.samajackun.rodas.core.model.IterableTableData;
import com.samajackun.rodas.core.model.RowData;

public class CollectionIterableTableData implements IterableTableData
{
	private final List<ColumnMetaData> metadata;

	private final Collection<Map<String, Object>> collection;

	public CollectionIterableTableData(List<ColumnMetaData> metadata, Collection<Map<String, Object>> collection)
	{
		super();
		this.metadata=metadata;
		this.collection=collection;
	}

	@Override
	public Iterator<RowData> iterator()
	{
		return new MyIterator<RowData>(this.collection.iterator());
	}

	private class MyIterator<T> implements Iterator<RowData>
	{
		private final Iterator<Map<String, Object>> src;

		private int position;

		public MyIterator(Iterator<Map<String, Object>> src)
		{
			super();
			this.src=src;
		}

		@Override
		public boolean hasNext()
		{
			return this.src.hasNext();
		}

		@Override
		public RowData next()
		{
			this.position++;
			return new MyRowData(this.src.next());
		}

		private class MyRowData implements RowData
		{
			private final Map<String, Object> map;

			public MyRowData(Map<String, Object> map)
			{
				super();
				this.map=map;
			}

			@Override
			public Object get(int column)
			{
				return this.map.get(CollectionIterableTableData.this.metadata.get(column).getName());
			}

			@Override
			public long position()
			{
				return MyIterator.this.position;
			}
		}
	}
}
