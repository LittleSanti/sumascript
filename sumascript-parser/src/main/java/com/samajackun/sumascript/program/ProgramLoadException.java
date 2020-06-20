package com.samajackun.sumascript.program;

public class ProgramLoadException extends Exception
{
	private static final long serialVersionUID=3737783759962088538L;

	public ProgramLoadException()
	{
	}

	public ProgramLoadException(String message)
	{
		super(message);
	}

	public ProgramLoadException(Throwable cause)
	{
		super(cause);
	}

	public ProgramLoadException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ProgramLoadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
