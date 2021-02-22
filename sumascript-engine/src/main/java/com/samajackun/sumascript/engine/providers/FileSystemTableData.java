package com.samajackun.sumascript.engine.providers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.RowData;
import com.samajackun.rodas.core.model.TableData;

public class FileSystemTableData implements TableData
{
	private final List<RowData> files;

	public FileSystemTableData(File path)
	{
		super();
		File[] list=path.listFiles();
		List<RowData> files;
		if (list == null)
		{
			files=Collections.emptyList();
		}
		else
		{
			files=new ArrayList<>(list.length);
			int i=0;
			for (File file : list)
			{
				files.add(new FileSystemRowData(file, i++));
			}
		}
		this.files=files;
	}

	@Override
	public Iterator<RowData> iterator()
	{
		return this.files.iterator();
	}

	@Override
	public Object[] getRow(int row)
		throws ProviderException
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public int countRows()
		throws ProviderException
	{
		return this.files.size();
	}

}
