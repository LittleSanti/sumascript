package com.samajackun.sumascript.parser;

import com.samajackun.rodas.sql.parser.GenericArithmeticExpressionParser;
import com.samajackun.rodas.sql.parser.GenericComparisonExpressionParser;
import com.samajackun.rodas.sql.parser.GenericExpressionCollectionParser;
import com.samajackun.rodas.sql.parser.GenericExpressionListParser;
import com.samajackun.rodas.sql.parser.GenericExpressionParser;
import com.samajackun.rodas.sql.parser.GenericLogicalExpressionParser;
import com.samajackun.rodas.sql.parser.GenericRelationalExpressionParser;
import com.samajackun.rodas.sql.parser.GenericSelectSentenceParser;
import com.samajackun.rodas.sql.parser.ParserFactory;
import com.samajackun.rodas.sql.parser.PartialParser;

public final class SumaParserFactory implements ParserFactory
{
	private GenericExpressionParser expressionParser;

	private GenericArithmeticExpressionParser arithmeticExpressionParser;

	private GenericComparisonExpressionParser comparisonExpressionParser;

	private GenericExpressionListParser expressionListParser;

	private GenericExpressionCollectionParser expressionCollectionParser;

	private GenericLogicalExpressionParser logicalExpressionParser;

	private GenericRelationalExpressionParser relationalExpressionParser;

	private GenericSelectSentenceParser selectSentenceParser;

	private static final SumaParserFactory INSTANCE=new SumaParserFactory();

	public static SumaParserFactory getInstance()
	{
		return INSTANCE;
	}

	private SumaParserFactory()
	{
	}

	@Override
	public GenericExpressionParser getExpressionParser()
	{
		if (this.expressionParser == null)
		{
			synchronized (this)
			{
				if (this.expressionParser == null)
				{
					this.expressionParser=SumaExpressionParser.getInstance();
				}
			}
		}
		return this.expressionParser;
	}

	@Override
	public GenericArithmeticExpressionParser getArithmeticExpressionParser()
	{
		if (this.arithmeticExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.arithmeticExpressionParser == null)
				{
					this.arithmeticExpressionParser=SumaArithmeticExpressionParser.getInstance();
				}
			}
		}
		return this.arithmeticExpressionParser;
	}

	@Override
	public GenericComparisonExpressionParser getComparisonExpressionParser()
	{
		if (this.comparisonExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.comparisonExpressionParser == null)
				{
					this.comparisonExpressionParser=SumaComparisonExpressionParser.getInstance();
				}
			}
		}
		return this.comparisonExpressionParser;
	}

	@Override
	public GenericExpressionListParser getExpressionListParser()
	{
		if (this.expressionListParser == null)
		{
			synchronized (this)
			{
				if (this.expressionListParser == null)
				{
					this.expressionListParser=SumaExpressionListParser.getInstance();
				}
			}
		}
		return this.expressionListParser;
	}

	@Override
	public GenericExpressionCollectionParser getExpressionCollectionParser()
	{
		if (this.expressionCollectionParser == null)
		{
			synchronized (this)
			{
				if (this.expressionCollectionParser == null)
				{
					this.expressionCollectionParser=SumaExpressionCollectionParser.getInstance();
				}
			}
		}
		return this.expressionCollectionParser;
	}

	@Override
	public PartialParser getLogicalExpressionParser()
	{
		if (this.logicalExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.logicalExpressionParser == null)
				{
					this.logicalExpressionParser=SumaLogicalExpressionParser.getInstance();
				}
			}
		}
		return this.logicalExpressionParser;
	}

	@Override
	public PartialParser getRelationalExpressionParser()
	{
		if (this.relationalExpressionParser == null)
		{
			synchronized (this)
			{
				if (this.relationalExpressionParser == null)
				{
					this.relationalExpressionParser=SumaRelationalExpressionParser.getInstance();
				}
			}
		}
		return this.relationalExpressionParser;
	}

	@Override
	public PartialParser getSelectSentenceParser()
	{
		if (this.selectSentenceParser == null)
		{
			synchronized (this)
			{
				if (this.selectSentenceParser == null)
				{
					this.selectSentenceParser=SumaSelectSentenceParser.getInstance();
				}
			}
		}
		return this.selectSentenceParser;
	}
}
