package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.io.Writer;

public final class SumaProgramSerializer
{
	private static final SumaProgramSerializer INSTANCE=new SumaProgramSerializer();

	public static SumaProgramSerializer getInstance()
	{
		return INSTANCE;
	}

	private SumaProgramSerializer()
	{
	}

	public void serialize(Program program, Writer output)
		throws IOException
	{
		// TODO
		throw new UnsupportedOperationException();
	}

}
