package com.samajackun.sumascript.parser;

public final class InstructionsParser
{
	private static final InstructionsParser INSTANCE=new InstructionsParser();

	private InstructionsParser()
	{
	}

	public static InstructionsParser getInstance()
	{
		return INSTANCE;
	}

}
