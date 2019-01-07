package com.samajackun.sumascript.core.instructions;

import java.io.File;

import com.samajackun.sumascript.core.ExecutionException;

public class UnexistingPathException extends ExecutionException
{
	private static final long serialVersionUID=5961956356344015409L;

	private final File path;

	public UnexistingPathException(File path)
	{
		super("Unexisting path " + path.getAbsolutePath());
		this.path=path;
	}

	public File getPath()
	{
		return this.path;
	}
}
