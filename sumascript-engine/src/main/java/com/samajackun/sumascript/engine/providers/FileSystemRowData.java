package com.samajackun.sumascript.engine.providers;

import java.io.File;
import java.util.Date;

import com.samajackun.rodas.core.model.RowData;

public class FileSystemRowData implements RowData
{
	private final int pos;

	private final File file;

	public FileSystemRowData(File file, int pos)
	{
		super();
		this.pos=pos;
		this.file=file;
	}

	@Override
	public Object get(int column)
	{
		switch (column)
		{
			case 0:
				return this.file.getName();
			case 1:
				return this.file.length();
			case 2:
				return new Date(this.file.lastModified());
			default:
				throw new IllegalStateException();
		}
	}

	@Override
	public long position()
	{
		return this.pos;
	}

}
