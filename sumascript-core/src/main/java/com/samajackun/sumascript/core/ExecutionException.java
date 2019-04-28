package com.samajackun.sumascript.core;

public class ExecutionException extends Exception
{
	private static final long serialVersionUID=867342756619314949L;

	private Location location;

	private int line;

	public ExecutionException(String message)
	{
		super(message);
	}

	public ExecutionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ExecutionException(Throwable cause)
	{
		super(cause.getMessage(), cause);
	}

	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(Location location)
	{
		this.location=location;
	}

	public int getLine()
	{
		return this.line;
	}

	public void setLine(int line)
	{
		this.line=line;
	}

	@Override
	public String getMessage()
	{
		return super.getMessage() + " at " + this.location + ":" + this.line;
	}

}
