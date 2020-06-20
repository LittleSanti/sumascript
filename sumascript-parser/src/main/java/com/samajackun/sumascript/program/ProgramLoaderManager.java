package com.samajackun.sumascript.program;

import java.util.SortedMap;
import java.util.TreeMap;

public class ProgramLoaderManager
{
	private static final ProgramLoaderManager INSTANCE=new ProgramLoaderManager();

	public static ProgramLoaderManager getInstance()
	{
		return INSTANCE;
	}

	private ProgramLoaderManager()
	{
	}

	private final SortedMap<VersionId, ProgramLoader> loadersMap=createMap();

	public ProgramLoader getLoader(VersionId loaderId)
		throws ProgramLoaderNotFoundException
	{
		ProgramLoader loader=this.loadersMap.get(loaderId);
		if (loader == null)
		{
			throw new ProgramLoaderNotFoundException(loaderId);
		}
		return loader;

	}

	private SortedMap<VersionId, ProgramLoader> createMap()
	{
		SortedMap<VersionId, ProgramLoader> map=new TreeMap<>();
		map.put(Versions.TEXT_SOURCE_CODE, TextSourceCodeProgramLoader.getInstance());
		map.put(Versions.BINARY_CODE_1_0, new BinaryCodeProgramLoader(Versions.BINARY_CODE_1_0));
		return map;
	}

	public ProgramLoader getNewestLoader()
	{
		return this.loadersMap.get(this.loadersMap.lastKey());
	}
}
