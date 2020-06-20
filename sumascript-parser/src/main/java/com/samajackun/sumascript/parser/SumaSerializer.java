package com.samajackun.sumascript.parser;

import java.io.IOException;
import java.io.Writer;

public final class SumaSerializer
{
	private static final SumaSerializer INSTANCE=new SumaSerializer();

	public static SumaSerializer getInstance()
	{
		return INSTANCE;
	}

	private SumaSerializer()
	{
	}

	public void serialize(Program program, Writer output)
		throws IOException
	{
		// TODO
		throw new UnsupportedOperationException();
	}

}
