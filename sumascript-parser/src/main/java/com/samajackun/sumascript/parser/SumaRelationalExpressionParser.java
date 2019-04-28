package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericRelationalExpressionParser;

public final class SumaRelationalExpressionParser extends GenericRelationalExpressionParser
{
	private static final SumaRelationalExpressionParser INSTANCE=new SumaRelationalExpressionParser();

	public static SumaRelationalExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaRelationalExpressionParser()
	{
		super(SumaParserFactory.getInstance());
	}
}
