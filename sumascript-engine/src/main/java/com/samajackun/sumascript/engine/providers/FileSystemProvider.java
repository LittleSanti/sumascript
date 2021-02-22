package com.samajackun.sumascript.engine.providers;

import java.io.File;

import com.samajackun.rodas.core.model.Provider;
import com.samajackun.rodas.core.model.ProviderException;
import com.samajackun.rodas.core.model.TableMetaData;

public class FileSystemProvider implements Provider
{
	@Override
	public TableMetaData getTableMetaData(String table)
		throws ProviderException
	{
		File file=new File(table);
		if (file.isDirectory() && file.exists())
		{
			return new FileSystemTableMetadata(table, file);
		}
		else
		{
			throw new ProviderException("Input is not a valid path: " + table);
		}
	}

}
