package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericComparisonExpressionParser;

public final class SumaComparisonExpressionParser extends GenericComparisonExpressionParser
{
	private static final SumaComparisonExpressionParser INSTANCE=new SumaComparisonExpressionParser();

	public static SumaComparisonExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaComparisonExpressionParser()
	{
		super(SumaParserFactory.getInstance());
	}

}
