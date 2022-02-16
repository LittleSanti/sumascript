package com.samajackun.sumascript.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import com.samajackun.rodas.core.model.Expression;
import com.samajackun.rodas.core.model.IdentifierExpression;
import com.samajackun.rodas.parsing.parser.ParserException;
import com.samajackun.rodas.parsing.source.PushBackSource;
import com.samajackun.rodas.parsing.source.ReaderSource;
import com.samajackun.rodas.sql.parser.ParserContext;
import com.samajackun.sumascript.expressions.TernaryExpression;
import com.samajackun.sumascript.tokenizer.SumaMatchingTokenizer;
import com.samajackun.sumascript.tokenizer.SumaTokenizer;

public class SumaExpressionParserTest
{
	private Expression parse(String code)
		throws ParserException,
		IOException
	{
		SumaMatchingTokenizer tokenizer=new SumaMatchingTokenizer(new SumaTokenizer(new PushBackSource(new ReaderSource(new StringReader(code)))));
		ParserContext parserContext=new ParserContext();
		return SumaParser.getInstance().parseExpression(tokenizer, parserContext);
	}

	@Test
	public void ternary()
		throws ParserException,
		IOException
	{
		Expression expression=parse("a ? b : c");
		assertTrue(expression instanceof TernaryExpression);
		TernaryExpression ternaryExpression=(TernaryExpression)expression;
		assertEquals("a", ((IdentifierExpression)ternaryExpression.getConditional()).getIdentifier());
		assertEquals("b", ((IdentifierExpression)ternaryExpression.getDirect()).getIdentifier());
		assertEquals("c", ((IdentifierExpression)ternaryExpression.getInverse()).getIdentifier());
	}
}
