package com.samajackun.sumascript.program;

public class ProgramLoaderNotFoundException extends RuntimeException
{
	private static final long serialVersionUID=-7523771163544777699L;

	private final VersionId loaderId;

	public ProgramLoaderNotFoundException(VersionId loaderId)
	{
		super("Loader id '" + loaderId + "' not supported");
		this.loaderId=loaderId;
	}

	public VersionId getLoaderId()
	{
		return this.loaderId;
	}
}
