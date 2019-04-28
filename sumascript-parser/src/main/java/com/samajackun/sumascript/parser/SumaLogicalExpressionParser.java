package com.samajackun.sumascript.parser;

import java.io.IOException;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.sql.parser.GenericLogicalExpressionParser;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.rodas.sql.tokenizer.AbstractMatchingTokenizer;
import com.samajackun.rodas.sql.tokenizer.Token;
import com.samajackun.sumascript.expressions.TernaryExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenTypes;

public final class SumaLogicalExpressionParser extends GenericLogicalExpressionParser
{
	private static final SumaLogicalExpressionParser INSTANCE=new SumaLogicalExpressionParser();

	public static SumaLogicalExpressionParser getInstance()
	{
		return INSTANCE;
	}

	private SumaLogicalExpressionParser()
	{
		super(SumaParserFactory.getInstance());
	}

	@Override
	protected Expression unexpectedToken(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Token token, Expression inputExpression)
		throws ParserException,
		IOException
	{
		Expression expression;
		switch (token.getType())
		{
			case SumaTokenTypes.OPERATOR_TERNARY:
				expression=parseTernary(tokenizer, parserContext, inputExpression);
				break;
			default:
				expression=super.unexpectedToken(tokenizer, parserContext, token, inputExpression);
				break;
		}
		return expression;
	}

	private TernaryExpression parseTernary(AbstractMatchingTokenizer tokenizer, ParserContext parserContext, Expression conditional)
		throws ParserException,
		IOException
	{
		Expression direct=SumaParser.getInstance().parseExpression((SumaMatchingTokenizer)tokenizer, parserContext);
		tokenizer.matchToken(SumaTokenTypes.COLON);
		Expression inverse=SumaParser.getInstance().parseExpression((SumaMatchingTokenizer)tokenizer, parserContext);
		TernaryExpression ternary=new TernaryExpression(conditional, direct, inverse);
		return ternary;
	}
}
