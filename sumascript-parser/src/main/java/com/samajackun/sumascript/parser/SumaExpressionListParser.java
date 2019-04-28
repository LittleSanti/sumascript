package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericExpressionListParser;

public final class SumaExpressionListParser extends GenericExpressionListParser
{
	private static final SumaExpressionListParser INSTANCE=new SumaExpressionListParser();

	public static SumaExpressionListParser getInstance()
	{
		return INSTANCE;
	}

	private SumaExpressionListParser()
	{
		super(SumaParserFactory.getInstance());
	}

}
