package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericExpressionCollectionParser;

public final class SumaExpressionCollectionParser extends GenericExpressionCollectionParser
{
	private static final SumaExpressionCollectionParser INSTANCE=new SumaExpressionCollectionParser();

	public static SumaExpressionCollectionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaExpressionCollectionParser()
	{
		super(SumaParserFactory.getInstance());
	}
}
