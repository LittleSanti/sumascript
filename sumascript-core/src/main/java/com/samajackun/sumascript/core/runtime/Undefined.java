package com.samajackun.sumascript.core.runtime;

public class Undefined
{
	private Undefined()
	{
	}

	public static final Undefined INSTANCE=new Undefined();

	public static Undefined getInstance()
	{
		return Undefined.INSTANCE;
	}
}
