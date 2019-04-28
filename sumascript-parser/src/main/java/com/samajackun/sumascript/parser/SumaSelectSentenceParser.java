package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericSelectSentenceParser;

public final class SumaSelectSentenceParser extends GenericSelectSentenceParser
{
	private static final SumaSelectSentenceParser INSTANCE=new SumaSelectSentenceParser();

	public static SumaSelectSentenceParser getInstance()
	{
		return INSTANCE;
	}

	private SumaSelectSentenceParser()
	{
		super(SumaParserFactory.getInstance());
	}
}
